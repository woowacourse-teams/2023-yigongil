package com.yigongil.backend.domain.study;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface StudyRepository extends Repository<Study, Long> {

    Study save(Study study);

    Optional<Study> findById(Long studyId);

    Page<Study> findAllByProcessingStatus(ProcessingStatus processingStatus, Pageable pageable);

    @Query("""
            select distinct s from Study s join fetch s.rounds rs where s.id = :id
            """)
    Optional<Study> findByIdWithRound(@Param("id") Long id);
}
