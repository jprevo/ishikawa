package org.sitsgo.ishikawa.announcement.game;

import org.springframework.data.repository.CrudRepository;

public interface GameAnnouncementRepository extends CrudRepository<GameAnnouncement, Long> {

    GameAnnouncement findTopByGameIdAndServer(String gameId, String server);

}
