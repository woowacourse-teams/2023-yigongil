package com.yigongil.backend.application;

import com.yigongil.backend.domain.feedpost.FeedPost;
import com.yigongil.backend.domain.feedpost.FeedPostRepository;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.study.PageStrategy;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.request.RegularFeedPostCreateRequest;
import com.yigongil.backend.response.FeedPostResponse;
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
    public void createFeedPost(Member member, Study study, RegularFeedPostCreateRequest request) {
        FeedPost regularFeedPost = FeedPost.builder()
                                           .author(member)
                                           .study(study)
                                           .imageUrl(request.imageUrl())
                                           .content(request.content())
                                           .build();
        feedPostRepository.save(regularFeedPost);
    }

    @Transactional(readOnly = true)
    public List<FeedPostResponse> findFeedPosts(Long studyId, int page) {
        return feedPostRepository.findAllByStudyId(studyId, PageStrategy.defaultPageStrategy(page))
                                 .stream()
                                 .map(FeedPostResponse::from)
                                 .toList();
    }
}
