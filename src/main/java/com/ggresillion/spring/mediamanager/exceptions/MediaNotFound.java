package com.ggresillion.spring.mediamanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MediaNotFound extends Exception {

    private static final String DEFAULT_MSG = "Media not found";

    public MediaNotFound() {
        super(DEFAULT_MSG);
    }

    public MediaNotFound(String message) {
        super(message);
    }
}
