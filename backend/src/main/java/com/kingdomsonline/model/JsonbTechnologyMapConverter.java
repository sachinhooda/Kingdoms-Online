package com.kingdomsonline.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kingdomsonline.model.TechnologyState;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.HashMap;
import java.util.Map;

@Converter(autoApply = false)
public class JsonbTechnologyMapConverter implements AttributeConverter<Map<String, TechnologyState>, String> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, TechnologyState> techMap) {
        try {
            return mapper.writeValueAsString(techMap);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert tech map to JSON", e);
        }
    }

    @Override
    public Map<String, TechnologyState> convertToEntityAttribute(String json) {
        try {
            return mapper.readValue(json, new TypeReference<Map<String, TechnologyState>>() {});
        } catch (Exception e) {
            return new HashMap<>();
        }
    }
}
