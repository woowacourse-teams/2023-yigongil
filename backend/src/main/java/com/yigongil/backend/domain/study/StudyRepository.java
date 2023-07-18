package com.yigongil.backend.domain.study;

import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface StudyRepository extends Repository<Study, Long> {

    Study save(Study study);

    Optional<Study> findById(Long studyId);
}
