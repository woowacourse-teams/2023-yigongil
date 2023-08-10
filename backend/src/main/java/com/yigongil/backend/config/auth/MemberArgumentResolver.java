package com.yigongil.backend.config.auth;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.member.MemberRepository;
import com.yigongil.backend.exception.AuthorizationException;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberRepository memberRepository;
    private final AuthContext authContext;

    public MemberArgumentResolver(MemberRepository memberRepository, AuthContext authContext) {
        this.memberRepository = memberRepository;
        this.authContext = authContext;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(Member.class)
                && parameter.hasParameterAnnotation(Authorization.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        Long possibleMemberId = authContext.getMemberId();
        return memberRepository.findById(possibleMemberId)
                               .orElseThrow(
                                       () -> new AuthorizationException("인증된 사용자가 아닙니다.", String.valueOf(possibleMemberId)
                                       ));
    }
}
