package com.yigongil.backend.domain.study;

import com.yigongil.backend.domain.member.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudyRepository extends JpaRepository<Study, Long> {

    Study save(Study study);

    @EntityGraph(attributePaths = {"rounds"})
    Optional<Study> findById(Long studyId);

    Page<Study> findAllByProcessingStatus(ProcessingStatus processingStatus, Pageable pageable);

    Page<Study> findAllByProcessingStatusAndNameContainingIgnoreCase(ProcessingStatus processingStatus, String word, Pageable pageable);

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
            join s.rounds r
            where r.master.id = :masterId
            and s.processingStatus = :status
            """)
    List<Study> findAllByMasterIdAndProcessingStatus(
            @Param("masterId") Long masterId,
            @Param("status") ProcessingStatus status
    );

//    @Modifying
//    @Query("""
//            delete from Study s
//            where s in :studies
//            """
//    )
//    void deleteAll(@Param("studies") List<Study> studies);
}
