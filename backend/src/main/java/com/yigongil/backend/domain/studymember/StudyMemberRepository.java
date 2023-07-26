package com.yigongil.backend.domain.studymember;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface StudyMemberRepository extends Repository<StudyMember, Long> {

    StudyMember save(StudyMember studyMember);

    @Query("""
            select sm from StudyMember sm
            join fetch sm.member
            join fetch sm.study
            where sm.studyResult = 'NONE'
            and sm.member.id = :memberId
            and sm.study.id = :studyId
            """)
    Optional<StudyMember> findByStudyIdAndMemberIdAndParticipatingAndNotEnd(Long studyId, Long memberId);

    @EntityGraph(attributePaths = "member")
    List<StudyMember> findAllByStudyIdAndRole(Long studyId, Role role);

    @Query("""
                select sm from StudyMember sm
                join fetch sm.member
                where sm.study.id = :studyId
                and (sm.role = 'MASTER'
                or sm.role = 'STUDY_MEMBER')
                and sm.studyResult = 'NONE'
            """)
    List<StudyMember> findAllByStudyIdAndParticipatingAndNotEnd(Long studyId);

    boolean existsByStudyIdAndMemberId(Long studyId, Long memberId);

    void delete(StudyMember studyMember);

    @EntityGraph(attributePaths = "study")
    @Query("""
            select sm from StudyMember sm
            join fetch sm.study
            where sm.member.id = :memberId
            and (sm.role = 'MASTER'
            or sm.role = 'STUDY_MEMBER')
            and sm.studyResult = 'NONE'
            """)
    List<StudyMember> findAllByMemberIdAndParticipatingAndNotEnd(Long memberId);
}
