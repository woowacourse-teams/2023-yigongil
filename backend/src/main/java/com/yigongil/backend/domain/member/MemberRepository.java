package com.yigongil.backend.domain.member;

import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface MemberRepository extends Repository<Member, Long> {

    Optional<Member> findById(Long id);

    Member save(Member member);
}
