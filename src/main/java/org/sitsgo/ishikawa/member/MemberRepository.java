package org.sitsgo.ishikawa.member;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MemberRepository extends CrudRepository<Member, Long> {
    Member findById(long id);

    Member findByDiscordId(long id);

    Member findByFfgId(int id);

    List<Member> findByOgsIdIsNotNull();
}
