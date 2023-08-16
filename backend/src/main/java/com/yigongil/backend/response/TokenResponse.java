package com.yigongil.backend.response;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {

}
