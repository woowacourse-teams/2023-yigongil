package com.yigongil.backend.domain.study.studyquery;

import com.yigongil.backend.domain.study.ProcessingStatus;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.domain.studymember.Role;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface StudyQueryRepository {

    Slice<Study> findStudiesByConditions(String search, ProcessingStatus status, Pageable page);

    Slice<Study> findStudiesApplied(Long memberId, String search, Role role, Pageable page);
}
