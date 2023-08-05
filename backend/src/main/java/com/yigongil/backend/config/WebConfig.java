package com.yigongil.backend.config;

import com.yigongil.backend.config.auth.AuthInterceptor;
import com.yigongil.backend.config.auth.MemberArgumentResolver;
import com.yigongil.backend.ui.querycounter.LoggingInterceptor;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final MemberArgumentResolver memberArgumentResolver;
    private final LoginInterceptor loginInterceptor;
    private final LoggingInterceptor loggingInterceptor;

    public WebConfig(
            MemberArgumentResolver memberArgumentResolver,
            LoginInterceptor loginInterceptor,
            LoggingInterceptor loggingInterceptor
    ) {
        this.memberArgumentResolver = memberArgumentResolver;
        this.loginInterceptor = loginInterceptor;
        this.loggingInterceptor = loggingInterceptor;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/v1/**")
                .excludePathPatterns("/v1/login/**")
                .excludePathPatterns("/v1/members/{id:[0-9]\\d*}")
                .excludePathPatterns("/v1/studies/recruiting");
      
        registry.addInterceptor(loggingInterceptor)
                .addPathPatterns("/**")
                .order(3);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberArgumentResolver);
    }
}
