package com.yigongil.backend.domain.studymember;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface StudyMemberRepository extends Repository<StudyMember, Long> {

    StudyMember save(StudyMember studyMember);

    @EntityGraph(attributePaths = {"member", "study"})
    Optional<StudyMember> findByStudyIdAndMemberId(Long studyId, Long memberId);

    @EntityGraph(attributePaths = "member")
    List<StudyMember> findAllByStudyIdAndRole(Long studyId, Role role);

    @EntityGraph(attributePaths = "study")
    List<StudyMember> findAllByMemberId(Long memberId);

    @Query("""
                select sm from StudyMember sm
                join fetch sm.member
                where sm.study.id = :studyId
                and (sm.role = 'MASTER'
                or sm.role = 'STUDY_MEMBER')
                and sm.studyResult = 'NONE'
            """)
    List<StudyMember> findAllByStudyIdAndParticipatingAndNotEnd(@Param("studyId") Long studyId);

    boolean existsByStudyIdAndMemberId(Long studyId, Long memberId);

    void delete(StudyMember studyMember);

    @Query("""
            select sm from StudyMember sm
            join fetch sm.study
            where sm.member.id = :memberId
            and (sm.role = 'MASTER'
            or sm.role = 'STUDY_MEMBER')
            and sm.studyResult = 'NONE'
            """)
    List<StudyMember> findAllByMemberIdAndParticipatingAndNotEnd(@Param("memberId") Long memberId);

    Long countByMemberIdAndStudyResult(Long memberId, StudyResult studyResult);

    void deleteAllByStudyIdAndRole(Long studyId, Role role);
}
