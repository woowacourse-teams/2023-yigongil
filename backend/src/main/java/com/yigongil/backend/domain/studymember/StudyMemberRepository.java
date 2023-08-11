package com.yigongil.backend.domain.studymember;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;

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
}
