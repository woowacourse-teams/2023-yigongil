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
import com.yigongil.backend.domain.member.MemberRepository;
import com.yigongil.backend.fixture.MemberFixture;
import com.yigongil.backend.request.StudyCreateRequest;
import com.yigongil.backend.request.TodoCreateRequest;
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

    @BeforeEach
    void setUp() {
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(MemberFixture.김진우.toMember()));    }

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
    void 투두를_생성한다() throws Exception {
        TodoCreateRequest request = new TodoCreateRequest(true, 1L, "첫 투두");

        willReturn(1L).given(todoService).create(MemberFixture.김진우.toMember(),1L, request);

        mockMvc.perform(post("/v1/studies/1/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/v1/studies/1/todos/1"));
    }
}
