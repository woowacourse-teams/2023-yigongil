package com.yigongil.backend.domain.round;

import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface RoundRepository extends Repository<Round, Long> {

    @EntityGraph(attributePaths = "roundOfMembers")
    Optional<Round> findById(Long id);

    @Query("""
                    select r from Round r
                    join fetch r.roundOfMembers
                    where r.id = :roundId
            """)
    Optional<Round> findRoundByIdWithRoundsOfMember(@Param("roundId") Long roundId);
}
