package com.yigongil.backend.ui.exceptionhandler;

public interface InternalServerErrorMessageConverter {

    String convert(Exception e);
}
