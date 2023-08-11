package com.yigongil.backend.ui;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yigongil.backend.application.StudyService;
import com.yigongil.backend.application.TodoService;
import com.yigongil.backend.config.auth.AuthContext;
import com.yigongil.backend.config.oauth.JwtTokenProvider;
import com.yigongil.backend.domain.member.MemberRepository;
import com.yigongil.backend.fixture.MemberFixture;
import com.yigongil.backend.request.StudyUpdateRequest;
import com.yigongil.backend.ui.exceptionhandler.InternalServerErrorMessageConverter;
import com.yigongil.backend.utils.querycounter.ApiQueryCounter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(StudyController.class)
class StudyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudyService studyService;

    @MockBean
    private TodoService todoService;

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
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(MemberFixture.김진우.toMember()));
    }

    @Test
    void 스터디를_개설한다() throws Exception {
        LocalDate startAt = LocalDate.now().plus(5L, ChronoUnit.MONTHS);
        StudyUpdateRequest request = new StudyUpdateRequest(
                "자바",
                5,
                startAt,
                14,
                "1w",
                "안녕"
        );

        willReturn(1L).given(studyService).create(MemberFixture.김진우.toMember(), request);

        mockMvc.perform(post("/v1/studies")
                       .header(HttpHeaders.AUTHORIZATION, "1")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(request)))
               .andDo(print())
               .andExpect(status().isCreated())
               .andExpect(header().string(HttpHeaders.LOCATION, "/v1/studies/1"));
    }

    @Test
    void 스터디의_예상시작일을_과거로_설정하고_개설하면_예외가_발생한다() throws Exception {
        LocalDate pastDate = LocalDate.now().minusDays(2L);
        StudyUpdateRequest request = new StudyUpdateRequest(
                "자바",
                5,
                pastDate,
                14,
                "1w",
                "안녕"
        );

        willReturn(1L).given(studyService).create(MemberFixture.김진우.toMember(), request);

        mockMvc.perform(post("/v1/studies")
                       .header(HttpHeaders.AUTHORIZATION, "1")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(request)))
               .andDo(print())
               .andExpect(status().isBadRequest());
    }
}
