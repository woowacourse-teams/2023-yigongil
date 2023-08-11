package com.yigongil.backend.domain.roundofmember;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;

public interface RoundOfMemberRepository extends Repository<RoundOfMember, Long> {

    @EntityGraph(attributePaths = "member")
    List<RoundOfMember> findAllByIdIn(List<Long> ids);
}
