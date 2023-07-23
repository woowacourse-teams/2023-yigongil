package com.yigongil.backend.domain.studymember;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.study.Role;
import com.yigongil.backend.domain.study.Study;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface StudyMemberRepository extends Repository<StudyMember, Long> {

    StudyMember save(StudyMember studyMember);

    Optional<StudyMember> findByStudyIdAndMemberId(Long studyId, Long memberId);

    @EntityGraph(attributePaths = "member")
    List<StudyMember> findAllByStudyIdAndRole(Long studyId, Role role);

    boolean existsByStudyAndMember(Study study, Member member);

    void delete(StudyMember studyMember);
}
