package com.kingdomsonline.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "config")
@Getter
@Setter
public class Config {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String key;

    @Column(columnDefinition = "jsonb", nullable = false)
    private String value;

}
