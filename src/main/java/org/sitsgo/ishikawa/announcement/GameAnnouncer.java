package org.sitsgo.ishikawa.announcement;

import org.sitsgo.ishikawa.discord.DiscordBot;
import org.sitsgo.ishikawa.go.Game;
import org.sitsgo.ishikawa.goserver.GoServerType;
import org.sitsgo.ishikawa.goserver.kgs.KgsGoServer;
import org.sitsgo.ishikawa.goserver.ogs.OgsServer;
import org.sitsgo.ishikawa.member.Member;
import org.sitsgo.ishikawa.member.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@EnableScheduling
public class GameAnnouncer {
    private static final Logger log = LoggerFactory.getLogger(GameAnnouncer.class);

    private final DiscordBot bot;

    private final KgsGoServer kgs;

    private final OgsServer ogs;

    private final MemberRepository memberRepository;

    private final AnnouncementRepository announcementRepository;

    public GameAnnouncer(
            DiscordBot bot,
            KgsGoServer kgs,
            OgsServer ogs,
            MemberRepository memberRepository,
            AnnouncementRepository announcementRepository) {
        this.bot = bot;
        this.kgs = kgs;
        this.ogs = ogs;
        this.memberRepository = memberRepository;
        this.announcementRepository = announcementRepository;
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

    private void runAnnouncements(ArrayList<Game> games) {
        games.removeIf(this::shouldNotAnnounce);

        games.forEach(game -> {
            bot.announceGame(game);
            persistAnnouncement(game);
        });
    }

    private boolean shouldNotAnnounce(Game game) {
        return isNotClubGame(game) || isAlreadyAnnounced(game);
    }

    private void persistAnnouncement(Game game) {
        Announcement announcement = Announcement.createFromGame(game);
        announcementRepository.save(announcement);
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
        String serverName = Announcement.getServerName(game.getServerType());

        Announcement announcement = announcementRepository
                .findTopByGameIdAndServer(game.getId(), serverName);

        return announcement != null;
    }

}
