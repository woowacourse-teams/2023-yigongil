package com.yigongil.backend.domain.round;

import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;

public interface RoundRepository extends Repository<Round, Long> {

    @EntityGraph(attributePaths = "roundOfMembers")
    Optional<Round> findById(Long id);
}
