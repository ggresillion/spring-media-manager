package com.ggresillion.spring.mediamanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class MediaException extends Exception {

    private static final String DEFAULT_MSG = "There was an issue dealing with medias";

    public MediaException() {
        super(DEFAULT_MSG);
    }

    public MediaException(String message) {
        super(message);
    }
}
