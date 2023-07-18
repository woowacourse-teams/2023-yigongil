package com.yigongil.backend.domain.study;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface StudyRepository extends Repository<Study, Long> {

    Study save(Study study);

    @Query("""
            select distinct s from Study s join fetch s.rounds rs where s.id = :id
            """)
    Optional<Study> findById(Long id);
}
