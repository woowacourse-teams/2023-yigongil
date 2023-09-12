package com.yigongil.backend.domain.feedpost;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

public interface FeedPostRepository extends Repository<FeedPost, Long> {

    Page<FeedPost> findAllByStudyId(Long studyId, Pageable pageable);
}
