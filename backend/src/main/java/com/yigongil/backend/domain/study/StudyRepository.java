package com.yigongil.backend.domain.study;

import com.yigongil.backend.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudyRepository extends Repository<Study, Long> {

    Study save(Study study);

    Optional<Study> findById(Long studyId);

    Page<Study> findAllByProcessingStatus(ProcessingStatus processingStatus, Pageable pageable);

    @Query("""
            select distinct s from Study s join fetch s.rounds rs where s.id = :id
            """)
    Optional<Study> findByIdWithRound(@Param("id") Long id);

    @Query("""
                select distinct s from Study s
                join StudyMember sm
                on s = sm.study
                join fetch s.currentRound
                where sm.member = :member
                and s.processingStatus = :processingStatus
            """)
    List<Study> findByMemberAndProcessingStatus(@Param("member") Member member, @Param("processingStatus") ProcessingStatus processingStatus);

    @Query("""
                select distinct s from Study s
                join StudyMember sm
                on s = sm.study
                where sm.member = :member
            """)
    List<Study> findStartedStudiesByMember(@Param("member") Member member);

}
