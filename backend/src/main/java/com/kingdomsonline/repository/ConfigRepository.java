package com.kingdomsonline.repository;

import com.kingdomsonline.model.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ConfigRepository extends JpaRepository<Config, Long> {
    Optional<Config> findByKey(String key);
}
