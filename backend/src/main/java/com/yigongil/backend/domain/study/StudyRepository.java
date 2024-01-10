package com.yigongil.backend.domain.study;

import com.yigongil.backend.domain.member.domain.Member;
import com.yigongil.backend.domain.study.studymember.Role;
import com.yigongil.backend.domain.study.studyquery.StudyQueryRepository;
import com.yigongil.backend.exception.StudyNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface StudyRepository extends Repository<Study, Long>, StudyQueryRepository {

    Study save(Study study);

    Optional<Study> findById(Long studyId);

    @Query("""
                select distinct s from Study s
                join StudyMember sm
                on s = sm.study
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

    default Study getById(Long studyId) {
        return findById(studyId).orElseThrow(() -> new StudyNotFoundException("해당 스터디를 찾을 수 없습니다", studyId));
    }
}
