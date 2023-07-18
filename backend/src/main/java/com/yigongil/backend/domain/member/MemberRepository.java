package com.yigongil.backend.domain.member;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends Repository<Member, Long> {

    Optional<Member> findById(Long id);

    Member save(Member member);

    @Query("""
            select distinct rm.member from RoundOfMember rm
            join fetch Round r
            on r.id = :id
            """)
    List<Member> findMembersByRoundId(Long id);
}
