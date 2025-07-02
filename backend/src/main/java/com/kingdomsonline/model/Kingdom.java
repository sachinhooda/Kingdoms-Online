package com.kingdomsonline.model;

import com.kingdomsonline.config.JsonConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
public class Kingdom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    private int x;
    private int y;

    @Column(name = "honor")
    private Integer honor = 0;

    @Enumerated(EnumType.STRING)
    private Race race;

    @Column(columnDefinition = "jsonb")
    @ColumnTransformer(write = "?::jsonb")
    private String castle;

    @Column(columnDefinition = "jsonb")
    @ColumnTransformer(write = "?::jsonb")
    private String villages;

    @Column(columnDefinition = "jsonb")
    @ColumnTransformer(write = "?::jsonb")
    @Convert(converter = JsonbTechnologyMapConverter.class)
    private Map<String, TechnologyState> technology;
}
