package com.kingdomsonline.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kingdomsonline.model.Config;
import com.kingdomsonline.repository.ConfigRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConfigLoader {

    private final ConfigRepository configRepository;
    private final ObjectMapper objectMapper;
    private List<TechnologyRule> technologyRules;

    public ConfigLoader(ConfigRepository configRepository, ObjectMapper objectMapper) {
        this.configRepository = configRepository;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void load() {
        configRepository.findByKey("technology_rules").ifPresent(config -> {
            try {
                technologyRules = objectMapper.readValue(
                    config.getValue(),
                    new TypeReference<List<TechnologyRule>>() {}
                );
            } catch (Exception e) {
                throw new RuntimeException("Failed to load technology rules", e);
            }
        });
    }

    public List<TechnologyRule> getTechnologyRules() {
        return technologyRules;
    }
}
