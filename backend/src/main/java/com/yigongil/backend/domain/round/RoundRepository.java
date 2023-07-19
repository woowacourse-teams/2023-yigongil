package com.yigongil.backend.domain.round;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface RoundRepository extends Repository<Round, Long> {

    Optional<Round> findById(Long id);

    @Query("""
                    select r from Round r
                    join fetch r.roundOfMembers
                    where r.id = :roundId
            """)
    Optional<Round> findRoundByIdWithRoundsOfMember(Long roundId);
}
