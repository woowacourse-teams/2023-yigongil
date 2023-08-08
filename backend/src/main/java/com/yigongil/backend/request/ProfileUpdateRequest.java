package com.yigongil.backend.request;

import javax.validation.constraints.NotBlank;

public record ProfileUpdateRequest(
        @NotBlank(message = "회원 닉네임이 공백입니다.")
        String nickname,
        @NotBlank(message = "회원 소개가 공백입니다.")
        String introduction
) {

}
