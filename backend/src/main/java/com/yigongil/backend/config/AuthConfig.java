package com.yigongil.backend.config;

import static com.yigongil.backend.config.auth.path.HttpMethod.ANY;
import static com.yigongil.backend.config.auth.path.HttpMethod.GET;
import static com.yigongil.backend.config.auth.path.HttpMethod.POST;
import static com.yigongil.backend.config.auth.path.HttpMethod.PUT;

import com.yigongil.backend.config.auth.AuthInterceptor;
import com.yigongil.backend.config.auth.MemberArgumentResolver;
import com.yigongil.backend.config.auth.path.PathInterceptor;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
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
        registry.addInterceptor(loginInterceptor());
    }

    private HandlerInterceptor loginInterceptor() {
        return new PathInterceptor(authInterceptor)
                .includePath("/**", ANY)
                .excludePath("/login/github/tokens", GET)
                .excludePath("/login/tokens/refresh", POST)
                .excludePath("/login/fake/tokens", GET)
                .excludePath("/members/{id:[0-9]\\d*}", GET)
                .excludePath("/members/exists", GET)
                .excludePath("/members", GET)
                .excludePath("/api/**", GET)
                .excludePath("/api-docs/**", GET)
                .excludePath("/swagger-ui/**", GET)
                .excludePath("/actuator/**", GET)
                .excludePath("/fake/proceed", PUT)
                .excludePath("/studies", GET)
                .excludePath("/studies/{id:[0-9]\\d*}", GET);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberArgumentResolver);
    }
}
