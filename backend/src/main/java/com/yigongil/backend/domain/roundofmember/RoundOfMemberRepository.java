package com.yigongil.backend.domain.roundofmember;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface RoundOfMemberRepository extends Repository<RoundOfMember, Long> {

    @Query("""
                    select distinct rm from RoundOfMember rm
                    join fetch rm.member
                    where rm in :roundOfMembers
            """)
    List<RoundOfMember> findRoundOfMembersWithMember(List<RoundOfMember> roundOfMembers);
}
