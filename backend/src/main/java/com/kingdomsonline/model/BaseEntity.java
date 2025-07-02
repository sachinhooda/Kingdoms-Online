package com.kingdomsonline.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntity {

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.createdBy = getCurrentUser();
        this.updatedBy = getCurrentUser();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = getCurrentUser();
    }

    private String getCurrentUser() {
        try {
            return org.springframework.security.core.context.SecurityContextHolder.getContext()
                .getAuthentication().getName();
        } catch (Exception e) {
            return "system";
        }
    }
}
