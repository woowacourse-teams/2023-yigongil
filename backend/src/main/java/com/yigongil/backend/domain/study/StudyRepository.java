package com.yigongil.backend.domain.study;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.study.studyquery.StudyQueryRepository;
import com.yigongil.backend.domain.studymember.Role;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface StudyRepository extends Repository<Study, Long>, StudyQueryRepository {

    Study save(Study study);

    @EntityGraph(attributePaths = {"rounds"})
    Optional<Study> findById(Long studyId);

    List<Study> findAllByProcessingStatus(ProcessingStatus processingStatus);

    @Query("""
                select distinct s from Study s
                join StudyMember sm
                on s = sm.study
                join fetch s.rounds
                where sm.member = :member
                and s.processingStatus = :processingStatus
            """)
    List<Study> findByMemberAndProcessingStatus(@Param("member") Member member, @Param("processingStatus") ProcessingStatus processingStatus);

    @Query("""
            select distinct s from Study s
            join StudyMember sm on s = sm.study
            where sm.member.id = :masterId
            and sm.role = :role
            and s.processingStatus = :status
            """)
    List<Study> findAllByMasterIdAndProcessingStatus(
            @Param("masterId") Long masterId,
            @Param("status") ProcessingStatus status,
            @Param("role") Role role
    );

    @Modifying
    @Query("""
            delete from Study s
            where s in :studies
            """)
    void deleteAll(@Param("studies") Iterable<Study> studies);
}
