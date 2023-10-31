package com.yigongil.backend.domain.member;

import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface MemberRepository extends Repository<Member, Long> {

    Optional<Member> findByIdAndDeletedFalse(Long id);

    Optional<Member> findByGithubId(String githubId);

    Member save(Member member);

    boolean existsByNickname(Nickname nickname);

    void delete(Member member);
}
