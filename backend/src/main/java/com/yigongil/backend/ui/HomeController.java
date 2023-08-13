package com.yigongil.backend.ui;

import com.yigongil.backend.application.RoundService;
import com.yigongil.backend.config.auth.Authorization;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.response.HomeResponse;
import com.yigongil.backend.ui.doc.HomeApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/home")
@RestController
public class HomeController implements HomeApi {

    private final RoundService roundService;

    public HomeController(RoundService roundService) {
        this.roundService = roundService;
    }

    @GetMapping
    public ResponseEntity<HomeResponse> home(@Authorization Member member) {
        HomeResponse response = roundService.findCurrentRoundOfStudies(member);
        return ResponseEntity.ok(response);
    }
}
