package com.ggresillion.spring.mediamanager.dto;

import org.springframework.http.MediaType;

import java.io.InputStream;

public class MediaContent {
    private int id;
    private InputStream content;
    private MediaType type;

    public int getId() {
        return id;
    }

    public MediaContent setId(int id) {
        this.id = id;
        return this;
    }

    public InputStream getContent() {
        return content;
    }

    public MediaContent setContent(InputStream content) {
        this.content = content;
        return this;
    }

    public MediaType getType() {
        return type;
    }

    public MediaContent setType(MediaType type) {
        this.type = type;
        return this;
    }
}
