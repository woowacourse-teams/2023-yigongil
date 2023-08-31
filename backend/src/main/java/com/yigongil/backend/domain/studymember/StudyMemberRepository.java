package com.yigongil.backend.domain.studymember;

import com.yigongil.backend.domain.study.Study;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface StudyMemberRepository extends Repository<StudyMember, Long> {

    StudyMember save(StudyMember studyMember);

    @EntityGraph(attributePaths = {"member", "study"})
    Optional<StudyMember> findByStudyIdAndMemberId(Long studyId, Long memberId);

    @EntityGraph(attributePaths = "member")
    List<StudyMember> findAllByStudyIdAndRole(Long studyId, Role role);

    @EntityGraph(attributePaths = "member")
    List<StudyMember> findAllByStudyId(Long studyId);

    @EntityGraph(attributePaths = "study")
    List<StudyMember> findAllByMemberId(Long memberId);

    @EntityGraph(attributePaths = "member")
    List<StudyMember> findAllByStudyIdAndRoleNotAndStudyResult(Long studyId, Role role, StudyResult studyResult);

    @EntityGraph(attributePaths = "study")
    List<StudyMember> findAllByMemberIdAndRoleNotAndStudyResult(Long memberId, Role role, StudyResult studyResult);

    boolean existsByStudyIdAndMemberId(Long studyId, Long memberId);

    void delete(StudyMember studyMember);

    Long countByMemberIdAndStudyResult(Long memberId, StudyResult studyResult);

    void deleteAllByStudyIdAndRole(Long studyId, Role role);

    @Modifying
    @Query("""
            delete from StudyMember sm
            where sm.study in :studies
            and sm.member.id = :memberId
            """)
    void deleteByStudyInAndMemberId(@Param("studies") List<Study> studies, @Param("memberId") Long memberId);
}
