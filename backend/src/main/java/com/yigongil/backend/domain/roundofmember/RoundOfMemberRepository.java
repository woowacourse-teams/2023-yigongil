package com.yigongil.backend.domain.roundofmember;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface RoundOfMemberRepository extends Repository<RoundOfMember, Long> {

    @Query("""
                    select distinct rm from RoundOfMember rm
                    join fetch rm.member
                    where rm in :roundOfMembers
            """)
    List<RoundOfMember> findRoundOfMembersWithMember(@Param("roundOfMembers") List<RoundOfMember> roundOfMembers);
}
