package com.yigongil.backend.config;

import com.yigongil.backend.config.auth.LoginInterceptor;
import com.yigongil.backend.config.auth.MemberArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final MemberArgumentResolver memberArgumentResolver;
    private final LoginInterceptor loginInterceptor;

    public WebConfig(MemberArgumentResolver memberArgumentResolver, LoginInterceptor loginInterceptor) {
        this.memberArgumentResolver = memberArgumentResolver;
        this.loginInterceptor = loginInterceptor;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/v1/members");
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberArgumentResolver);
    }
}
