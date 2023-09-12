package com.yigongil.backend.domain.feedpost.regularfeedpost;

import java.util.List;
import org.springframework.data.repository.Repository;

public interface RegularFeedPostRepository extends Repository<RegularFeedPost, Long> {

    List<RegularFeedPost> findAllByIdIn(List<Long> ids);
}
