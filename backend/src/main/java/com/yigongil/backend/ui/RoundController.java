package com.yigongil.backend.ui;

import com.yigongil.backend.application.RoundService;
import com.yigongil.backend.config.auth.Authorization;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.response.RoundResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/studies/{studyId}/rounds/{roundId}")
@RestController
public class RoundController {

    private final RoundService roundService;

    public RoundController(final RoundService roundService) {
        this.roundService = roundService;
    }

    @GetMapping
    public ResponseEntity<RoundResponse> viewRoundDetail(
            @Authorization Member member,
            @PathVariable Long studyId,
            @PathVariable Long roundId
    ) {
        RoundResponse response = roundService.findRoundDetail(member, roundId);
        return ResponseEntity.ok(response);
    }
}
