package com.ggresillion.spring.mediamanager.repositories;

import com.ggresillion.spring.mediamanager.models.MediaScaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaScaleRepository extends JpaRepository<MediaScaleEntity, Integer> {
}
