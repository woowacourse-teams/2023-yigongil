package com.yigongil.backend.request;

import org.hibernate.validator.constraints.Length;

public record ProfileUpdateRequest(
        @Length(min = 2, max = 8, message = "회원의 닉네임 길이는 2자 이상 8자 이하로 허용됩니다.")
        String nickname,
        @Length(max = 200, message = "회원 소개글의 길이는 200자를 넘을 수 없습니다.")
        String introduction
) {

}
