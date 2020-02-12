package com.ggresillion.spring.mediamanager.dto;

import org.springframework.http.MediaType;

import java.io.InputStream;

public class Media {
    private int id;
    private MediaType type;

    public int getId() {
        return id;
    }

    public Media setId(int id) {
        this.id = id;
        return this;
    }

    public MediaType getType() {
        return type;
    }

    public Media setType(MediaType type) {
        this.type = type;
        return this;
    }
}
