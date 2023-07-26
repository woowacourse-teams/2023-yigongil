package com.yigongil.backend.domain.studymember;

import com.yigongil.backend.domain.study.Role;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface StudyMemberRepository extends Repository<StudyMember, Long> {

    StudyMember save(StudyMember studyMember);

    @EntityGraph(attributePaths = {"member", "study"})
    Optional<StudyMember> findByStudyIdAndMemberId(Long studyId, Long memberId);

    @EntityGraph(attributePaths = "member")
    List<StudyMember> findAllByStudyIdAndRole(Long studyId, Role role);

    @EntityGraph(attributePaths = "member")
    List<StudyMember> findAllByStudyIdAndRoleNot(Long studyId, Role role);

    boolean existsByStudyIdAndMemberId(Long studyId, Long memberId);

    void delete(StudyMember studyMember);

    @EntityGraph(attributePaths = "study")
    List<StudyMember> findAllByMemberIdAndRoleNot(Long id, Role role);
}
