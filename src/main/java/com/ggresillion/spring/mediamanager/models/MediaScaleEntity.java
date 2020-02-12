package com.ggresillion.spring.mediamanager.models;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "media_scale")
public class MediaScaleEntity<T extends MediaScaleEntity<T>> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(columnDefinition = "media_id")
    private MediaEntity media;

    private UUID uuid;

    private String size;

    public int getId() {
        return id;
    }

    public MediaScaleEntity<T> setId(int id) {
        this.id = id;
        return this;
    }

    public UUID getUuid() {
        return uuid;
    }

    public MediaScaleEntity<T> setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getSize() {
        return size;
    }

    public MediaScaleEntity<T> setSize(String size) {
        this.size = size;
        return this;
    }

    public MediaEntity getMedia() {
        return media;
    }

    public MediaScaleEntity<T> setMedia(MediaEntity media) {
        this.media = media;
        return this;
    }
}
