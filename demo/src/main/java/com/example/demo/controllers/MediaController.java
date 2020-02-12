package com.example.demo.controllers;

import com.ggresillion.spring.mediamanager.dto.Media;
import com.ggresillion.spring.mediamanager.dto.MediaContent;
import com.ggresillion.spring.mediamanager.exceptions.MediaException;
import com.ggresillion.spring.mediamanager.services.MediaService;
import com.google.common.io.ByteStreams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class MediaController {

    @Autowired
    private MediaService mediaService;

    @GetMapping("/medias")
    public Page<Media> getMedias(Pageable pageable) throws MediaException {
        return mediaService.getMedias(pageable);
    }

    @GetMapping("/medias/{id}")
    public ResponseEntity<byte[]> getMedia(@PathVariable int id, @RequestParam(required = false) String size) throws MediaException {
        MediaContent media = mediaService.getMedia(id, size);
        try {
            byte[] bytes = ByteStreams.toByteArray(media.getContent());
            return ResponseEntity.ok()
                    .contentType(media.getType())
                    .body(bytes);
        } catch (IOException e) {
            throw new MediaException(e.getMessage());
        }
    }

    @PostMapping("/medias")
    public MediaContent postMedia(@RequestParam("file") MultipartFile file) throws MediaException {
        return mediaService.createMedia(file);
    }

    @DeleteMapping("/medias/{id}")
    public void deleteMedia(@PathVariable int id) {
        mediaService.deleteMediaById(id);
    }
}
