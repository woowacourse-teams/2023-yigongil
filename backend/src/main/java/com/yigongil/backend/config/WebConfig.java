package com.yigongil.backend.config;

import com.yigongil.backend.config.auth.AuthInterceptor;
import com.yigongil.backend.config.auth.MemberArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final MemberArgumentResolver memberArgumentResolver;
    private final AuthInterceptor authInterceptor;

    public WebConfig(MemberArgumentResolver memberArgumentResolver, AuthInterceptor authInterceptor) {
        this.memberArgumentResolver = memberArgumentResolver;
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/v1/**")
                .excludePathPatterns("/v1/login/**")
                .excludePathPatterns("/v1/members/{id:[0-9]\\d*}")
                .excludePathPatterns("/v1/studies/recruiting");
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberArgumentResolver);
    }
}
