package com.yigongil.backend.ui;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yigongil.backend.application.MemberService;
import com.yigongil.backend.config.WebConfig;
import com.yigongil.backend.config.auth.AuthContext;
import com.yigongil.backend.config.oauth.JwtTokenProvider;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.member.MemberRepository;
import com.yigongil.backend.fixture.MemberFixture;
import com.yigongil.backend.request.ProfileUpdateRequest;
import com.yigongil.backend.response.NicknameValidationResponse;
import com.yigongil.backend.response.ProfileResponse;
import com.yigongil.backend.ui.exceptionhandler.InternalServerErrorMessageConverter;
import com.yigongil.backend.utils.querycounter.ApiQueryCounter;
import java.util.Collections;
import java.util.Optional;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@Import(WebConfig.class)
@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private AuthContext authContext;

    @MockBean
    private JwtTokenProvider tokenProvider;

    @MockBean
    private ApiQueryCounter apiQueryCounter;

    @MockBean
    private InternalServerErrorMessageConverter internalServerErrorMessageConverter;

    @Test
    void 프로필_정보를_조회한다() throws Exception {
        Member member = MemberFixture.김진우.toMember();
        given(memberRepository.findById(1L)).willReturn(Optional.of(member));
        given(memberService.findById(1L)).willReturn(
                new ProfileResponse(
                        member.getId(),
                        member.getNickname(),
                        member.getGithubId(),
                        member.getProfileImageUrl(),
                        0.0,
                        0,
                        0,
                        member.getTier(),
                        member.getIntroduction(),
                        Collections.emptyList()
                )
        );

        mockMvc.perform(get("/v1/members/1"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.nickname").value(member.getNickname()))
               .andExpect(jsonPath("$.githubId").value(member.getGithubId()));
    }

    @Test
    void 프로필_정보를_업데이트_한다() throws Exception {
        ProfileUpdateRequest request = new ProfileUpdateRequest("수정된김진우", "수정된자기소개");

        willDoNothing().given(memberService).update(any(), any());

        given(tokenProvider.parseToken(any())).willReturn(1L);
        given(memberRepository.findById(1L)).willReturn(Optional.of(MemberFixture.김진우.toMember()));
        given(authContext.getMemberId()).willReturn(1L);

        mockMvc.perform(patch("/v1/members")
                       .contentType(MediaType.APPLICATION_JSON)
                       .header(HttpHeaders.AUTHORIZATION, "Bearer 1")
                       .content(objectMapper.writeValueAsString(request)))
               .andDo(print())
               .andExpect(status().isOk());

        verify(memberService, only()).update(MemberFixture.김진우.toMember(), request);
    }

    @Test
    void 중복된_닉네임이_있는지_확인한다() throws Exception {
        final String existNickname = "jinwoo";
        given(memberService.existsByNickname(existNickname)).willReturn(new NicknameValidationResponse(true));

        mockMvc.perform(get("/v1/members/exists?nickname={nickname}", existNickname))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.exists").value(true));
    }
}
