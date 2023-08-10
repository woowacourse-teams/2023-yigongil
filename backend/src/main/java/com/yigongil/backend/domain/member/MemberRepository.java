package com.yigongil.backend.domain.member;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends Repository<Member, Long> {

    Optional<Member> findById(Long id);

    Optional<Member> findByGithubId(String githubId);

    Member save(Member member);

    @Query("""
            select distinct rm.member from RoundOfMember rm
            join fetch Round r
            on r.id = :id
            """)
    List<Member> findMembersByRoundId(@Param("id") Long id);

    boolean existsByNickname(Nickname nickname);
}
