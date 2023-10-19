package com.yigongil.backend.ui;

import com.yigongil.backend.application.AuthService;
import com.yigongil.backend.request.TokenRequest;
import com.yigongil.backend.response.TokenResponse;
import com.yigongil.backend.ui.doc.LoginApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/login")
@RestController
public class LoginController implements LoginApi {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/github/tokens")
    public ResponseEntity<TokenResponse> createMemberToken(@RequestParam String code) {
        TokenResponse response = authService.login(code);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tokens/validate")
    public ResponseEntity<Void> validateToken() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/tokens/refresh")
    public ResponseEntity<TokenResponse> refreshMemberToken(@RequestBody TokenRequest request) {
        TokenResponse response = authService.refresh(request);
        return ResponseEntity.ok(response);
    }
}

