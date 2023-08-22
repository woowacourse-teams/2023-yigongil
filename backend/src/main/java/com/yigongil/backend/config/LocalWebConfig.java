package com.yigongil.backend.config;

import com.yigongil.backend.config.auth.AuthInterceptor;
import com.yigongil.backend.config.auth.MemberArgumentResolver;
import com.yigongil.backend.utils.querycounter.LoggingInterceptor;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Profile(value = {"local", "test"})
@Configuration
public class LocalWebConfig implements WebMvcConfigurer {

    private final MemberArgumentResolver memberArgumentResolver;
    private final AuthInterceptor authInterceptor;
    private final LoggingInterceptor loggingInterceptor;

    public LocalWebConfig(MemberArgumentResolver memberArgumentResolver,
            AuthInterceptor authInterceptor, LoggingInterceptor loggingInterceptor) {
        this.memberArgumentResolver = memberArgumentResolver;
        this.authInterceptor = authInterceptor;
        this.loggingInterceptor = loggingInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/v1/**")
                .excludePathPatterns("/v1/login/**")
                .excludePathPatterns("/v1/members/{id:[0-9]\\d*}")
                .excludePathPatterns("/v1/members/exists")
                .excludePathPatterns("/v1/studies/recruiting/**")
                .excludePathPatterns("/v1/fake/proceed")
                .excludePathPatterns("/v1/api/**")
                .excludePathPatterns("/v1/api-docs/**")
                .excludePathPatterns("/v1/swagger-ui/**")
                .excludePathPatterns("/v1/studies/{id:[0-9]\\d*}/rounds/{id:[0-9]\\d*}/progress-rate")
                .order(2);

        registry.addInterceptor(loggingInterceptor)
                .addPathPatterns("/**")
                .order(3);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberArgumentResolver);
    }
}
