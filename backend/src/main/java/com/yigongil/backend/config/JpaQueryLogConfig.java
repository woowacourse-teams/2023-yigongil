package com.yigongil.backend.config;

import com.yigongil.backend.utils.querycounter.LoggingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Profile(value = {"local", "test"})
@Configuration
public class JpaQueryLogConfig implements WebMvcConfigurer {

    private final LoggingInterceptor loggingInterceptor;

    public JpaQueryLogConfig(LoggingInterceptor loggingInterceptor) {
        this.loggingInterceptor = loggingInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor)
                .addPathPatterns("/**");
    }
}
