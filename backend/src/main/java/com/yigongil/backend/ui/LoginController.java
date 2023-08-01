package com.yigongil.backend.ui;

import com.yigongil.backend.application.OauthService;
import com.yigongil.backend.response.TokenResponse;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/login")
@RestController
public class LoginController {

    private final OauthService oauthService;

    public LoginController(OauthService oauthService) {
        this.oauthService = oauthService;
    }

    @GetMapping("/github")
    public ResponseEntity<Void> loginGithub() {
        URI githubRedirectUrl = oauthService.getGithubRedirectUrl();
        return ResponseEntity.status(HttpStatus.FOUND).location(githubRedirectUrl).build();
    }

    @GetMapping("/github/tokens")
    public ResponseEntity<TokenResponse> createMemberToken(@RequestParam String code) {
        TokenResponse response = oauthService.login(code);
        return ResponseEntity.ok(response);
    }
}

