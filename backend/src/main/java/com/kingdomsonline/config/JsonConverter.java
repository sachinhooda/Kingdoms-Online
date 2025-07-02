package com.kingdomsonline.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;

@Converter(autoApply = false)
public class JsonConverter implements AttributeConverter<Object, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Object attribute) {
        try {
            return mapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Could not convert to JSON string", e);
        }
    }

    @Override
    public Object convertToEntityAttribute(String dbData) {
        try {
            return mapper.readValue(dbData, Object.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read JSON from DB", e);
        }
    }
}
