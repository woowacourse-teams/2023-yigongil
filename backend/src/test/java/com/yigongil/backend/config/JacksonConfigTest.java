package com.yigongil.backend.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.context.annotation.Import;

@JsonTest
@Import(JacksonConfig.class)
class JacksonConfigTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 문자열로_전달된_JSON을_LocalDate_타입으로_파싱한다() throws JsonProcessingException {
        // given
        LocalDate startAt = LocalDate.now().plus(1L, ChronoUnit.DAYS);
        String startAtInput = startAt.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));

        String json = """
                {
                    "startAt":"%s"
                }
                """.formatted(startAtInput);

        // when
        TestDto dto = objectMapper.readValue(json, TestDto.class);

        // then
        assertThat(dto.getStartAt()).isEqualTo(startAt);
    }

    private static class TestDto {

        private LocalDate startAt;

        public LocalDate getStartAt() {
            return startAt;
        }
    }
}
