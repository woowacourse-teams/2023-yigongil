package com.yigongil.backend.domain.round;

import com.yigongil.backend.config.auth.Authorization;
import com.yigongil.backend.domain.member.domain.Member;
import com.yigongil.backend.response.UpcomingStudyResponse;
import com.yigongil.backend.ui.doc.HomeApi;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/home")
@RestController
public class HomeController implements HomeApi {

    private final RoundService roundService;

    public HomeController(RoundService roundService) {
        this.roundService = roundService;
    }

    @GetMapping
    public ResponseEntity<List<UpcomingStudyResponse>> home(@Authorization Member member) {
        List<UpcomingStudyResponse> response = roundService.findCurrentRoundOfStudies(member);
        return ResponseEntity.ok(response);
    }
}
