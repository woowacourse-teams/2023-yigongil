package com.yigongil.backend.ui;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.yigongil.backend.application.MemberService;
import com.yigongil.backend.config.WebConfig;
import com.yigongil.backend.domain.member.MemberRepository;
import com.yigongil.backend.fixture.MemberFixture;
import com.yigongil.backend.request.ProfileUpdateRequest;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Test
    void 프로필_정보를_업데이트_한다() throws Exception {
        ProfileUpdateRequest request = new ProfileUpdateRequest("수정된김진우", "수정된자기소개");

        willDoNothing().given(memberService).updateMember(any(), any());

        given(memberRepository.findById(1L)).willReturn(Optional.of(MemberFixture.김진우.toMember()));

        mockMvc.perform(patch("/v1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "1")
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(memberService, only()).updateMember(MemberFixture.김진우.toMember(), request);
    }
}