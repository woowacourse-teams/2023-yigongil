package com.yigongil.backend.ui;

import com.yigongil.backend.application.OauthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping
@RestController
public class LoginController {

    private final OauthService oauthService;

    public LoginController(OauthService oauthService) {
        this.oauthService = oauthService;
    }

    @GetMapping("/v1/login/github")
    public ResponseEntity<Long> login(
            @RequestParam String authCode
    ) {
        Long id = oauthService.login(authCode);
        return ResponseEntity.ok(id);
    }
}

