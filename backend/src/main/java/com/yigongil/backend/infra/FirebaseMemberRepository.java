package com.yigongil.backend.infra;

import java.util.List;
import org.springframework.data.repository.Repository;

public interface FirebaseMemberRepository extends Repository<FirebaseMember, Long> {

    void save(FirebaseMember firebaseMember);

    List<FirebaseMember> findAllByMemberId(Long memberId);
}
