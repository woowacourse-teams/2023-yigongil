package com.yigongil.backend.config.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Getter
@Setter
@RequestScope
@Component
public class AuthContext {

    private Long memberId;
}
