package com.yigongil.backend.ui.exceptionhandler;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile(value = "prod")
@Component
public class ProdInternalServerErrorMessageConverter implements InternalServerErrorMessageConverter {

    @Override
    public String convert(Exception e) {
        return "서버 에러 발생";
    }
}
