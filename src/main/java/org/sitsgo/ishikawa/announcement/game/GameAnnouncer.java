package org.sitsgo.ishikawa.announcement.game;

import org.sitsgo.ishikawa.go.Game;
import org.sitsgo.ishikawa.goserver.GoServerType;
import org.sitsgo.ishikawa.goserver.kgs.KgsGoServer;
import org.sitsgo.ishikawa.goserver.ogs.OgsServer;
import org.sitsgo.ishikawa.member.Member;
import org.sitsgo.ishikawa.member.MemberRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@EnableScheduling
public class GameAnnouncer implements ApplicationEventPublisherAware {

    private final KgsGoServer kgs;

    private final OgsServer ogs;

    private final MemberRepository memberRepository;

    private final GameAnnouncementRepository gameAnnouncementRepository;

    private ApplicationEventPublisher applicationEventPublisher;

    public GameAnnouncer(
            KgsGoServer kgs,
            OgsServer ogs,
            MemberRepository memberRepository,
            GameAnnouncementRepository gameAnnouncementRepository) {
        this.kgs = kgs;
        this.ogs = ogs;
        this.memberRepository = memberRepository;
        this.gameAnnouncementRepository = gameAnnouncementRepository;
    }

    @Scheduled(cron = "0/30 * * * * ?")
    public void checkOgs() {
        ArrayList<Game> games = ogs.getActiveGames();
        runAnnouncements(games);
    }

    @Scheduled(cron = "0 0/2 * * * ?")
    public void checkKgs() {
        ArrayList<Game> games = kgs.getActiveGames();
        runAnnouncements(games);
    }

    public void runAnnouncements(ArrayList<Game> games) {
        games.removeIf(this::shouldNotAnnounce);
        games.forEach(this::announceGame);
    }

    public void announceGame(Game game) {
        GameAnnouncementEvent event = new GameAnnouncementEvent(game);
        applicationEventPublisher.publishEvent(event);

        persistAnnouncement(game);
    }

    @Override
    public void setApplicationEventPublisher(@NonNull ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    private boolean shouldNotAnnounce(Game game) {
        return isNotClubGame(game) || isAlreadyAnnounced(game);
    }

    private void persistAnnouncement(Game game) {
        GameAnnouncement gameAnnouncement = GameAnnouncement.createFromGame(game);
        gameAnnouncementRepository.save(gameAnnouncement);
    }

    private boolean isNotClubGame(Game game) {
        Member black = getMemberMatchingUsername(game.getServerType(), game.getBlack());
        Member white = getMemberMatchingUsername(game.getServerType(), game.getWhite());

        if (black == null || white == null) {
            return true;
        }

        if (!black.getInClub()) {
            black = null;
        }

        if (!white.getInClub()) {
            white = null;
        }

        return white == null || black == null;
    }

    private Member getMemberMatchingUsername(GoServerType serverType, String username) {
        return switch (serverType) {
            case KGS -> memberRepository.findTopByKgsUsername(username);
            case OGS -> memberRepository.findTopByOgsUsername(username);
            default -> null;
        };
    }

    private boolean isAlreadyAnnounced(Game game) {
        GameAnnouncement gameAnnouncement = gameAnnouncementRepository
                .findTopByGameIdAndServer(game.getId(), game.getServerName());

        return gameAnnouncement != null;
    }

}
