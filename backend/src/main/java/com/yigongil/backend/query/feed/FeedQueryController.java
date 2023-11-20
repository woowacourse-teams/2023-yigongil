package com.yigongil.backend.query.feed;

import com.yigongil.backend.response.FeedPostResponse;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeedQueryController implements FeedQueryApi {

    private final FeedQueryService feedQueryService;

    public FeedQueryController(FeedQueryService feedQueryService) {
        this.feedQueryService = feedQueryService;
    }

    @GetMapping("/studies/{id}/feeds")
    public ResponseEntity<List<FeedPostResponse>> findFeedPosts(
        @PathVariable Long id,
        @RequestParam Optional<Long> oldestFeedPostId
    ) {
        List<FeedPostResponse> response = feedQueryService.findFeedPosts(
            id,
            oldestFeedPostId.orElse(Long.MAX_VALUE)
        );
        return ResponseEntity.ok(response);
    }
}
