package com.yigongil.backend.domain.study;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ProcessingStatusTest {

    @ValueSource(strings = {"all", "ALL", "PROCESSING", "processing", "end", "END", "recruiting", "RECRUITING"})
    @ParameterizedTest
    void 대소문자를_구분하지_않고_검증한다(String target) {
        assertThatCode(() -> ProcessingStatus.validate(target)).doesNotThrowAnyException();
    }
}
