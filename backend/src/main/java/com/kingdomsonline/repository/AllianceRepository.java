package com.kingdomsonline.repository;

import com.kingdomsonline.model.Alliance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AllianceRepository extends JpaRepository<Alliance, Long> {
    boolean existsByName(String name);
    boolean existsByTag(String tag);
    Page<Alliance> findByIsDeletedFalse(Pageable pageable);

    Page<Alliance> findByNameContainingIgnoreCaseOrTagContainingIgnoreCaseAndIsDeletedFalse(
            String name, String tag, Pageable pageable
    );
    Optional<Alliance> findByIdAndIsDeletedFalse(Long id);
}
