package com.yigongil.backend.ui.doc;

import com.yigongil.backend.request.TokenRequest;
import com.yigongil.backend.response.TokenResponse;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Hidden
public interface LoginApi {

    ResponseEntity<TokenResponse> createMemberToken(@RequestParam String code);

    ResponseEntity<TokenResponse> refreshMemberToken(@RequestBody TokenRequest request);
}
