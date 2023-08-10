package com.yigongil.backend.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    @Bean
    public ObjectMapper serializingObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer());
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        objectMapper.registerModule(javaTimeModule);
        return objectMapper;
    }

    static class LocalDateSerializer extends JsonSerializer<LocalDate> {
        @Override
        public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.format(FORMATTER));
        }
    }

    static class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
        @Override
        public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return LocalDate.parse(p.getValueAsString(), FORMATTER);
        }
    }
}