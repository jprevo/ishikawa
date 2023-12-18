package org.sitsgo.ishikawa.member;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MemberRepository extends CrudRepository<Member, Long> {
    Member findById(long id);

    Member findByDiscordId(long id);

    Member findByFfgId(int id);

    List<Member> findByOgsIdIsNotNull();

    Member findTopByKgsUsername(String kgsUsername);

    Member findTopByOgsUsername(String ogsUsername);

    @Query("SELECT m FROM Member m " +
            "WHERE (m.ffgName LIKE CONCAT('%', ?1, '%') OR m.discordDisplayName LIKE ?1 OR m.ogsUsername LIKE ?1 OR m.kgsUsername LIKE ?1 OR m.osrUsername LIKE ?1) " +
            "AND m.anonymous = false")
    Member search(String name);
}
