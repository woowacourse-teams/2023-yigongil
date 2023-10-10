package com.yigongil.backend.config;

import com.yigongil.backend.config.auth.AuthInterceptor;
import com.yigongil.backend.config.auth.MemberArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthConfig implements WebMvcConfigurer {

    private final MemberArgumentResolver memberArgumentResolver;
    private final AuthInterceptor authInterceptor;

    public AuthConfig(MemberArgumentResolver memberArgumentResolver, AuthInterceptor authInterceptor) {
        this.memberArgumentResolver = memberArgumentResolver;
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login/github/tokens")
                .excludePathPatterns("/login/tokens/refresh")
                .excludePathPatterns("/login/fake/tokens")
                .excludePathPatterns("/members/{id:[0-9]\\d*}")
                .excludePathPatterns("/members/exists")
                .excludePathPatterns("/studies")
                .excludePathPatterns("/api/**")
                .excludePathPatterns("/api-docs/**")
                .excludePathPatterns("/swagger-ui/**")
                .excludePathPatterns("/actuator/**")
                .excludePathPatterns("/fake/proceed")
                .excludePathPatterns("/studies/{id:[0-9]\\d*}/rounds/{id:[0-9]\\d*}/progress-rate");

    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberArgumentResolver);
    }
}
