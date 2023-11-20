package com.yigongil.backend.domain.round;

import com.yigongil.backend.config.auth.Authorization;
import com.yigongil.backend.domain.member.domain.Member;
import com.yigongil.backend.request.MustDoUpdateRequest;
import com.yigongil.backend.response.RoundResponse;
import com.yigongil.backend.response.UpcomingStudyResponse;
import com.yigongil.backend.ui.doc.HomeApi;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoundController implements HomeApi, RoundApi {

    private final RoundService roundService;

    public RoundController(RoundService roundService) {
        this.roundService = roundService;
    }

    @GetMapping("/home")
    public ResponseEntity<List<UpcomingStudyResponse>> home(@Authorization Member member) {
        List<UpcomingStudyResponse> response = roundService.findCurrentRoundOfStudies(member);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/rounds/{roundId}/todos")
    public ResponseEntity<Void> updateMustDo(
        @Authorization Member member,
        @PathVariable Long roundId,
        @RequestBody @Valid MustDoUpdateRequest request
    ) {
        roundService.updateMustDo(member, roundId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/studies/{studyId}/rounds")
    public ResponseEntity<List<RoundResponse>> findRoundDetailsOfWeek(
        @PathVariable Long studyId,
        @RequestParam Integer weekNumber
    ) {
        List<RoundResponse> roundResponses = roundService.findRoundDetailsOfWeek(studyId, weekNumber);
        return ResponseEntity.ok(roundResponses);
    }
}
