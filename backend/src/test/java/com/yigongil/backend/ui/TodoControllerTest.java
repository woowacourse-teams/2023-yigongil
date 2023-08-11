package com.yigongil.backend.ui;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yigongil.backend.application.TodoService;
import com.yigongil.backend.config.auth.AuthContext;
import com.yigongil.backend.config.oauth.JwtTokenProvider;
import com.yigongil.backend.domain.member.MemberRepository;
import com.yigongil.backend.fixture.MemberFixture;
import com.yigongil.backend.request.TodoCreateRequest;
import com.yigongil.backend.request.TodoUpdateRequest;
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

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
        given(memberRepository.findByIdAndDeletedFalse(anyLong())).willReturn(Optional.of(MemberFixture.김진우.toMember()));
    }

    @Test
    void 필수_투두를_생성한다() throws Exception {
        TodoCreateRequest request = new TodoCreateRequest("첫 투두");

        willDoNothing().given(todoService).createNecessaryTodo(MemberFixture.김진우.toMember(), 1L, request);

        mockMvc.perform(post("/v1/rounds/1/todos/necessary")
                       .header(HttpHeaders.AUTHORIZATION, "1")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(request)))
               .andDo(print())
               .andExpect(status().isOk());
    }

    @Test
    void 필수_투두를_업데이트한다() throws Exception {
        TodoUpdateRequest request = new TodoUpdateRequest(true, "수정");

        willDoNothing().given(todoService).updateNecessaryTodo(MemberFixture.김진우.toMember(), 1L, request);

        mockMvc.perform(patch("/v1/rounds/1/todos/necessary")
                       .header(HttpHeaders.AUTHORIZATION, "1")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(request)))
               .andDo(print())
               .andExpect(status().isNoContent());

        verify(todoService, only()).updateNecessaryTodo(MemberFixture.김진우.toMember(), 1L, request);
    }


    @Test
    void 선택_투두를_생성한다() throws Exception {
        TodoCreateRequest request = new TodoCreateRequest("첫 투두");

        willReturn(1L).given(todoService).createOptionalTodo(MemberFixture.김진우.toMember(), 1L, request);

        mockMvc.perform(post("/v1/rounds/1/todos/optional")
                       .header(HttpHeaders.AUTHORIZATION, "1")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(request)))
               .andDo(print())
               .andExpect(status().isCreated())
               .andExpect(header().exists(HttpHeaders.LOCATION));
    }

    @Test
    void 선택_투두를_업데이트한다() throws Exception {
        TodoUpdateRequest request = new TodoUpdateRequest(true, "수정");

        willDoNothing().given(todoService).updateOptionalTodo(MemberFixture.김진우.toMember(), 1L, 1L, request);

        mockMvc.perform(patch("/v1/rounds/1/todos/optional/1")
                       .header(HttpHeaders.AUTHORIZATION, "1")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(request)))
               .andDo(print())
               .andExpect(status().isNoContent());

        verify(todoService, only()).updateOptionalTodo(MemberFixture.김진우.toMember(), 1L, 1L, request);
    }
}
