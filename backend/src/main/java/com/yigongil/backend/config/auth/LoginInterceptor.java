package com.yigongil.backend.config.auth;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.member.MemberRepository;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    private MemberRepository memberRepository;

    public LoginInterceptor(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!request.getMethod().equalsIgnoreCase(HttpMethod.GET.toString())) {
            return true;
        }
        String id = request.getHeader(HttpHeaders.AUTHORIZATION);
        Optional<Member> member = memberRepository.findById(Long.valueOf(id));
        return member.isPresent();
    }
}
