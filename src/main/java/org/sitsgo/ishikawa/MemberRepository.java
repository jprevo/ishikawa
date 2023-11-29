package org.sitsgo.ishikawa;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MemberRepository extends CrudRepository<Member, Long> {

    List<Member> findByLastName(String lastName);

    Member findById(long id);
}
