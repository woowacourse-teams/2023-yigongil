package com.yigongil.backend.domain.study;

import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface StudyRepository extends Repository<Study, Long> {

    Optional<Study> findById(Long studyId);

    Study save(Study study);
}
