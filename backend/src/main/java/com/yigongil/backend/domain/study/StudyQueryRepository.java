package com.yigongil.backend.domain.study;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface StudyQueryRepository {

    Slice<Study> findStudiesByConditions(String search, String status, Pageable page);
}
