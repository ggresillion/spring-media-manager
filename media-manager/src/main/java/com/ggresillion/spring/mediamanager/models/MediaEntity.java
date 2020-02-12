package com.ggresillion.spring.mediamanager.models;

import com.ggresillion.spring.mediamanager.components.MediaListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "media")
@EntityListeners(MediaListener.class)
public class MediaEntity<T extends MediaEntity<T>> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String type;

    private UUID uuid;

    @OneToMany(mappedBy = "media")
    private List<MediaScaleEntity> mediaScales = new ArrayList<>();

    public int getId() {
        return id;
    }

    public T setId(int id) {
        this.id = id;
        return (T)this;
    }

    public String getType() {
        return type;
    }

    public T setType(String type) {
        this.type = type;
        return (T)this;
    }

    public UUID getUuid() {
        return uuid;
    }

    public MediaEntity<T> setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public List<MediaScaleEntity> getMediaScales() {
        return mediaScales;
    }
}
