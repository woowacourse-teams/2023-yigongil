package com.yigongil.backend.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;

public record ProfileUpdateRequest(
        @Schema(description = "수정할 닉네임", example = "김김진진우")
        @NotBlank(message = "회원 닉네임이 공백입니다.")
        String nickname,

        @Schema(description = "수정할 소개 내용", example = "잘부탁드려요")
        @NotBlank(message = "회원 소개가 공백입니다.")
        String introduction
) {

}
