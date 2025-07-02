package com.kingdomsonline.service;

import com.kingdomsonline.model.Coordinates;
import com.kingdomsonline.model.MapTile;
import com.kingdomsonline.repository.MapTileRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MapService {

    private final MapTileRepository mapTileRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public MapService(MapTileRepository mapTileRepository) {
        this.mapTileRepository = mapTileRepository;
    }

    @Transactional
    public Coordinates allocateSpawnTile(Long playerId) {
        Optional<MapTile> optionalTile = mapTileRepository.findRandomUnassignedForSpawn();
        if (optionalTile.isEmpty()) {
            throw new RuntimeException("No available spawn tile found.");
        }

        MapTile tile = optionalTile.get();
        tile.setType("castle");
        tile.setPlayerId(playerId);
        mapTileRepository.save(tile); // updates locked row

        return new Coordinates(tile.getX(), tile.getY());
    }
}
