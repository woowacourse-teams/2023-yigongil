package com.yigongil.backend.domain.study;

import org.springframework.data.repository.Repository;

public interface StudyRepository extends Repository<Study, Long> {

    Study save(Study study);
}
