package com.yigongil.backend.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yigongil.backend.config.auth.AuthContext;
import com.yigongil.backend.config.auth.JwtTokenProvider;
import com.yigongil.backend.domain.member.MemberRepository;
import com.yigongil.backend.ui.exceptionhandler.InternalServerErrorMessageConverter;
import com.yigongil.backend.utils.querycounter.ApiQueryCounter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UtilController.class)
class UtilControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
    void 버전을_체크한다() throws Exception {
        var result = mockMvc.perform(get("/check-version")
                                    .param("v", "1000000")
                                    .contentType(MediaType.APPLICATION_JSON))
                            .andDo(print())
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.shouldUpdate").value(false))
                            .andExpect(jsonPath("$.message").value("최신 버전입니다."));
    }

    @Test
    void 존재하지_않는_버전을_체크한다() throws Exception {
        var result = mockMvc.perform(get("/check-version")
                                    .param("v", "없는 버전")
                                    .contentType(MediaType.APPLICATION_JSON))
                            .andDo(print())
                            .andExpect(status().isBadRequest());
    }
}
