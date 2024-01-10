package com.yigongil.backend.query.profile.ui;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yigongil.backend.config.AuthConfig;
import com.yigongil.backend.config.auth.AuthContext;
import com.yigongil.backend.config.auth.JwtTokenProvider;
import com.yigongil.backend.domain.member.domain.Member;
import com.yigongil.backend.domain.member.domain.MemberRepository;
import com.yigongil.backend.fixture.MemberFixture;
import com.yigongil.backend.query.profile.ProfileController;
import com.yigongil.backend.query.profile.ProfileService;
import com.yigongil.backend.response.ProfileResponse;
import com.yigongil.backend.ui.exceptionhandler.InternalServerErrorMessageConverter;
import com.yigongil.backend.utils.querycounter.ApiQueryCounter;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@Import(AuthConfig.class)
@WebMvcTest(ProfileController.class)
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfileService profileService;

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
        given(memberRepository.findByIdAndDeletedFalse(1L)).willReturn(Optional.of(member));
        given(profileService.findById(1L)).willReturn(
                new ProfileResponse(
                        member.getId(),
                        member.getNickname(),
                        member.getGithubId(),
                        member.getProfileImageUrl(),
                        0.0,
                        0,
                        0,
                        member.getExperience(),
                        member.getIntroduction(),
                        Collections.emptyList()
                )
        );

        mockMvc.perform(get("/members/1"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.nickname").value(member.getNickname()))
               .andExpect(jsonPath("$.githubId").value(member.getGithubId()));
    }
}
