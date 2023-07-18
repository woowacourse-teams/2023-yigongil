package com.yigongil.backend.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yigongil.backend.application.StudyService;
import com.yigongil.backend.domain.member.MemberRepository;
import com.yigongil.backend.fixture.MemberFixture;
import com.yigongil.backend.request.StudyCreateRequest;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudyController.class)
class StudyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudyService studyService;

    @MockBean
    private MemberRepository memberRepository;

    @Test
    void 프로필_정보를_업데이트_한다() throws Exception {
        StudyCreateRequest request = new StudyCreateRequest(
                "자바",
                5,
                "2023.05.21",
                14,
                "1w",
                "안녕"
        );
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(MemberFixture.김진우.toMember()));

        willReturn(1L).given(studyService).create(MemberFixture.김진우.toMember(), request);

        mockMvc.perform(post("/v1/studies")
                        .header(HttpHeaders.AUTHORIZATION, "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/v1/studies/1"));
    }
}
