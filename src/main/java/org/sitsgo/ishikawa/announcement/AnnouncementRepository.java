package org.sitsgo.ishikawa.announcement;

import org.springframework.data.repository.CrudRepository;

public interface AnnouncementRepository extends CrudRepository<Announcement, Long> {

    Announcement findTopByGameIdAndServer(String gameId, String server);

}
