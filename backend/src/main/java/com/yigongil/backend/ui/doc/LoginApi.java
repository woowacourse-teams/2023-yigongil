package com.yigongil.backend.ui.doc;

import com.yigongil.backend.config.auth.Authorization;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.request.MessagingTokenRequest;
import com.yigongil.backend.request.RefreshTokenRequest;
import com.yigongil.backend.response.TokenResponse;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Hidden
public interface LoginApi {

    ResponseEntity<Void> registerDevice(
            @Authorization Member member,
            @RequestBody MessagingTokenRequest request
    );

    ResponseEntity<TokenResponse> createMemberToken(@RequestParam String code);

    ResponseEntity<TokenResponse> refreshMemberToken(@RequestBody RefreshTokenRequest request);
}
