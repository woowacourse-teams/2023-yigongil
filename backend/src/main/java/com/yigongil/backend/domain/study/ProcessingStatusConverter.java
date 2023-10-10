package com.yigongil.backend.domain.study;

import org.springframework.core.convert.converter.Converter;

public class ProcessingStatusConverter implements Converter<String, ProcessingStatus> {

    @Override
    public ProcessingStatus convert(String source) {
        return ProcessingStatus.of(source);
    }
}
