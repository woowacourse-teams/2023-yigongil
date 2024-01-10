package com.yigongil.backend.domain.member.domain;

import com.yigongil.backend.exception.MemberNotFoundException;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface MemberRepository extends Repository<Member, Long> {

    Optional<Member> findByIdAndDeletedFalse(Long id);

    default Member getById(Long id) {
        return findByIdAndDeletedFalse(id).orElseThrow(() -> new MemberNotFoundException(
                "해당 멤버가 존재하지 않습니다.", String.valueOf(id)
            )
        );
    }

    Optional<Member> findByGithubId(String githubId);

    Member save(Member member);

    boolean existsByNickname(Nickname nickname);

    void delete(Member member);
}
