package com.yigongil.backend.domain.feedpost;

import com.yigongil.backend.domain.member.domain.Member;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.request.FeedPostCreateRequest;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeedService {

    private final FeedPostRepository feedPostRepository;

    public FeedService(FeedPostRepository feedPostRepository) {
        this.feedPostRepository = feedPostRepository;
    }

    @Transactional
    public void createFeedPost(Member member, Study study, FeedPostCreateRequest request) {
        FeedPost regularFeedPost = FeedPost.builder()
                                           .author(member)
                                           .study(study)
                                           .imageUrl(request.imageUrl())
                                           .content(request.content())
                                           .build();
        feedPostRepository.save(regularFeedPost);
    }

    @Transactional(readOnly = true)
    public List<FeedPost> findFeedPosts(Long studyId, Long oldestFeedPostId) {
        return feedPostRepository.findAllByStudyIdStartWithOldestFeedPostId(studyId, oldestFeedPostId);
    }
}
