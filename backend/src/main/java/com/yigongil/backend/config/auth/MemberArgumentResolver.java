package com.yigongil.backend.config.auth;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.member.MemberRepository;
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

    // TODO: 2023/08/01 서비스에서 도메인 객체를 반환할 때 리팩토링 드가자~
    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        return memberRepository.findById(authContext.getMemberId())
                               .orElseThrow(IllegalArgumentException::new);
    }
}
