package com.yigongil.backend.domain.studymember;

import org.springframework.data.repository.Repository;

public interface StudyMemberRepository extends Repository<StudyMember, Long> {

    StudyMember save(StudyMember studyMember);
}
