package com.yigongil.backend.query.feed;

import com.yigongil.backend.domain.feedpost.FeedPostRepository;
import com.yigongil.backend.response.FeedPostResponse;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class FeedQueryService {

    private final FeedPostRepository feedPostRepository;

    public FeedQueryService(FeedPostRepository feedPostRepository) {
        this.feedPostRepository = feedPostRepository;
    }

    @Transactional(readOnly = true)
    public List<FeedPostResponse> findFeedPosts(Long studyId, Long oldestFeedPostId) {
        return feedPostRepository.findAllByStudyIdStartWithOldestFeedPostId(studyId, oldestFeedPostId)
                                 .stream()
                                 .map(FeedPostResponse::from)
                                 .toList();
    }
}
