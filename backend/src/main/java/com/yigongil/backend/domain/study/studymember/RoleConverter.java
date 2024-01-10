package com.yigongil.backend.domain.study.studymember;

import org.springframework.core.convert.converter.Converter;

public class RoleConverter implements Converter<String, Role> {

    @Override
    public Role convert(String source) {
        return Role.of(source);
    }

}
