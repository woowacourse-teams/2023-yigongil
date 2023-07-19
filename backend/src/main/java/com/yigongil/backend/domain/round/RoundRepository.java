package com.yigongil.backend.domain.round;

import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface RoundRepository extends Repository<Round, Long> {

    Optional<Round> findById(Long id);
}
