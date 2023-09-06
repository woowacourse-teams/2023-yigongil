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
                .addPathPatterns("/v1/**")
                .excludePathPatterns("/v1/login/github/tokens")
                .excludePathPatterns("/v1/login/tokens/refresh")
                .excludePathPatterns("/v1/login/fake/tokens")
                .excludePathPatterns("/v1/members/{id:[0-9]\\d*}")
                .excludePathPatterns("/v1/members/exists")
                .excludePathPatterns("/v1/studies/recruiting/**")
                .excludePathPatterns("/v1/api/**")
                .excludePathPatterns("/v1/api-docs/**")
                .excludePathPatterns("/v1/swagger-ui/**")
                .excludePathPatterns("/v1/actuator/**")
                .excludePathPatterns("/v1/fake/proceed")
                .excludePathPatterns("/v1/studies/{id:[0-9]\\d*}/rounds/{id:[0-9]\\d*}/progress-rate");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberArgumentResolver);
    }
}
