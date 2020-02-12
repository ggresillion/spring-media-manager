package com.ggresillion.spring.mediamanager.repositories;

import com.ggresillion.spring.mediamanager.models.MediaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<MediaEntity, Integer> {
}
