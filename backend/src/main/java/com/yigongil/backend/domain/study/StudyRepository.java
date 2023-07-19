package com.yigongil.backend.domain.study;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;


public interface StudyRepository extends Repository<Study, Long> {

    Study save(Study study);

    Page<Study> findAllByProcessingStatus(ProcessingStatus processingStatus, Pageable pageable);
}
