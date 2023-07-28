package com.yigongil.backend.request;

public record ProfileUpdateRequest(
        String nickname,
        String introduction
) {

}
