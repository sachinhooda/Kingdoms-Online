package com.kingdomsonline.repository;

import com.kingdomsonline.model.MapTile;
import com.kingdomsonline.model.MapTileId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface MapTileRepository extends JpaRepository<MapTile, MapTileId> {

    @Query(value = """
        SELECT * FROM map_tile 
        WHERE player_id IS NULL 
        ORDER BY random() 
        LIMIT 1 
        FOR UPDATE SKIP LOCKED
        """, nativeQuery = true)
    Optional<MapTile> findRandomUnassignedForSpawn();
}
