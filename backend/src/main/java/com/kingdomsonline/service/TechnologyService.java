package com.kingdomsonline.service;

import com.kingdomsonline.config.ConfigLoader;
import com.kingdomsonline.config.TechnologyRule;
import com.kingdomsonline.config.TechRequirement;
import com.kingdomsonline.model.Kingdom;
import com.kingdomsonline.model.TechnologyState;
import com.kingdomsonline.repository.KingdomRepository;
import com.kingdomsonline.dto.TechnologyViewDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TechnologyService {

    private final KingdomRepository kingdomRepository;
    private final ConfigLoader configLoader;

    public TechnologyService(KingdomRepository kingdomRepository, ConfigLoader configLoader) {
        this.kingdomRepository = kingdomRepository;
        this.configLoader = configLoader;
    }

    public List<TechnologyViewDto> getTechnologyView(Long playerId) {
        Kingdom kingdom = kingdomRepository.findByPlayerId(playerId)
            .orElseThrow(() -> new RuntimeException("Kingdom not found"));

        Map<String, TechnologyState> playerTechMap = kingdom.getTechnology();
        List<TechnologyRule> allRules = configLoader.getTechnologyRules();

        List<TechnologyViewDto> result = new ArrayList<>();

        for (TechnologyRule rule : allRules) {
            TechnologyState state = playerTechMap != null ? playerTechMap.get(rule.id()) : null;
            int currentLevel = state != null ? state.currentLevel() : 0;
            TechnologyViewDto.UpgradeInfo upgradeInfo = null;
            if (state != null && state.upgrade() != null) {
                upgradeInfo = new TechnologyViewDto.UpgradeInfo(
                        state.upgrade().startedAt(),
                        state.upgrade().finishedAt()
                );
            }
            TechRequirement nextReq = rule.requirements().stream()
                .filter(r -> r.level() == currentLevel + 1)
                .findFirst()
                .orElse(null);

            String nextEffect = nextReq != null ? nextReq.effect() : null;

            result.add(new TechnologyViewDto(
                    rule.id(),
                    rule.name(),
                    rule.description(),
                    currentLevel,
                    rule.maxLevel(),
                    upgradeInfo,
                    nextEffect,
                    nextReq
            ));
        }

        return result;
    }
}
