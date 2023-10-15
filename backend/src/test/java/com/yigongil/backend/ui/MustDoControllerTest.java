package com.yigongil.backend.ui;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yigongil.backend.application.MustDoService;
import com.yigongil.backend.config.auth.AuthContext;
import com.yigongil.backend.config.auth.JwtTokenProvider;
import com.yigongil.backend.domain.member.MemberRepository;
import com.yigongil.backend.fixture.MemberFixture;
import com.yigongil.backend.request.MustDoUpdateRequest;
import com.yigongil.backend.ui.exceptionhandler.InternalServerErrorMessageConverter;
import com.yigongil.backend.utils.querycounter.ApiQueryCounter;
import java.util.Optional;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MustDoController.class)
class MustDoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MustDoService mustDoService;

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

    @BeforeEach
    void setUp() {
        given(memberRepository.findByIdAndDeletedFalse(anyLong())).willReturn(Optional.of(MemberFixture.김진우.toMember()));
    }

    @Test
    void 머스트두를_생성한다() throws Exception {
        MustDoUpdateRequest request = new MustDoUpdateRequest("첫 머스트두");

        willDoNothing().given(mustDoService).updateMustDo(MemberFixture.김진우.toMember(), 1L, request);

        mockMvc.perform(put("/rounds/1/todos")
                       .header(HttpHeaders.AUTHORIZATION, "1")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(request)))
               .andDo(print())
               .andExpect(status().isOk());
    }

    @Test
    void 머스트두를_업데이트한다() throws Exception {
        MustDoUpdateRequest request = new MustDoUpdateRequest("수정");

        willDoNothing().given(mustDoService).updateMustDo(MemberFixture.김진우.toMember(), 1L, request);

        mockMvc.perform(put("/rounds/1/todos")
                       .header(HttpHeaders.AUTHORIZATION, "1")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(request)))
               .andDo(print())
               .andExpect(status().isOk());

        verify(mustDoService, only()).updateMustDo(MemberFixture.김진우.toMember(), 1L, request);
    }
}
