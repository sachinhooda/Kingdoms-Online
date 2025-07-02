package com.kingdomsonline.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "map_tile")
@IdClass(MapTileId.class)
public class MapTile {

    @Id
    private int x;

    @Id
    private int y;

    private String type;      // "castle", "village", or null
    private String terrain;   // e.g., "land", "water"
    private Long playerId;    // original/current owner
}
