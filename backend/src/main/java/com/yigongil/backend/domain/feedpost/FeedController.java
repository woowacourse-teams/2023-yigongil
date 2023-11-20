package com.yigongil.backend.domain.feedpost;

import com.yigongil.backend.config.auth.Authorization;
import com.yigongil.backend.domain.member.domain.Member;
import com.yigongil.backend.request.FeedPostCreateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeedController implements FeedApi {

    private final FeedService feedService;

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @PostMapping("/studies/{id}/feeds")
    public ResponseEntity<Void> createFeedPost(
        @Authorization Member member,
        @PathVariable Long id,
        @RequestBody FeedPostCreateRequest request
    ) {
        feedService.createFeedPost(member, id, request);
        return ResponseEntity.ok().build();
    }
}
