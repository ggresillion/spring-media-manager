package com.ggresillion.spring.mediamanager.services;

import com.ggresillion.spring.mediamanager.components.UploadClient;
import com.ggresillion.spring.mediamanager.configurations.MediaManagerConfiguration;
import com.ggresillion.spring.mediamanager.dto.Media;
import com.ggresillion.spring.mediamanager.dto.MediaContent;
import com.ggresillion.spring.mediamanager.exceptions.MediaException;
import com.ggresillion.spring.mediamanager.models.MediaEntity;
import com.ggresillion.spring.mediamanager.models.MediaScaleEntity;
import com.ggresillion.spring.mediamanager.repositories.MediaRepository;
import com.ggresillion.spring.mediamanager.repositories.MediaScaleRepository;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class MediaService {

    private final static String MEDIA_NOT_FOUND = "media not found";
    private final static String SIZE_NOT_FOUND = "size not found";

    Logger logger = LoggerFactory.getLogger(MediaScaleEntity.class);

    @Autowired
    private UploadClient uploadClient;
    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private MediaScaleRepository mediaScaleRepository;
    @Autowired
    private MediaManagerConfiguration configuration;

    @PostConstruct
    public void init() {
        logger.info("MEDIA: configured with sizes {}", configuration.getSizes());
    }

    /**
     * Get all medias
     *
     * @return content as InputStream
     * @throws MediaException media not found
     */
    public Page<Media> getMedias(Pageable pageable) throws MediaException {
        return mediaRepository.findAll(pageable).map(this::toDto);
    }

    /**
     * Get media content by id
     *
     * @param id media id
     * @return content as InputStream
     * @throws MediaException media not found
     */
    public MediaContent getMedia(int id) throws MediaException {
        MediaEntity<?> media = mediaRepository.findById(id)
                .orElseThrow(() -> new MediaException(MEDIA_NOT_FOUND));
        return toDto(media, uploadClient.getFile(media.getUuid()));
    }

    /**
     * Get media content by id and size
     *
     * @param id media id
     * @return content as InputStream
     * @throws MediaException media not found
     */
    public MediaContent getMedia(int id, String size) throws MediaException {
        if (size == null) {
            return getMedia(id);
        }
        MediaEntity<?> media = mediaRepository.findById(id)
                .orElseThrow(() -> new MediaException(MEDIA_NOT_FOUND));
        for (MediaScaleEntity<?> mediaScale : media.getMediaScales()) {
            if (mediaScale.getSize().equals(size)) {
                return toDto(media, uploadClient.getFile(mediaScale.getUuid()));
            }
        }
        throw new MediaException(SIZE_NOT_FOUND);
    }

    /**
     * Create a new media from a multipartfile
     *
     * @param file multipartfile
     * @return new media
     * @throws MediaException issue uploading media
     */
    public MediaContent createMedia(MultipartFile file) throws MediaException {
        UUID uuid = uploadClient.uploadFile(file);
        MediaEntity<?> media = new MediaEntity()
                .setType(file.getContentType())
                .setUuid(uuid);
        media = mediaRepository.save(media);
        for (Map.Entry<String, Integer> map : configuration.getSizes().entrySet()) {
            try {
                BufferedImage image = ImageIO.read(uploadClient.getFile(uuid));
                BufferedImage resized = Scalr.resize(image, map.getValue());
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                String type;
                if (media.getType().startsWith("image")) {
                    type = media.getType().replace("image/", "");
                } else {
                    type = "png";
                }
                ImageIO.write(resized, type, os);
                InputStream is = new ByteArrayInputStream(os.toByteArray());
                UUID suuid = uploadClient.uploadFile(is, file.getContentType());
                MediaScaleEntity<?> defaultScale = new MediaScaleEntity()
                        .setSize(map.getKey())
                        .setUuid(suuid)
                        .setMedia(media);
                MediaScaleEntity<?> smedia = mediaScaleRepository.save(defaultScale);
                media.getMediaScales().add(smedia);
                os.close();
            } catch (IOException e) {
                throw new MediaException(e.getMessage());
            }
        }
        try {
            return toDto(media, file.getInputStream());
        } catch (IOException e) {
            throw new MediaException(e.getMessage());
        }
    }

    public void deleteMediaById(int id) {
        mediaRepository.deleteById(id);
    }

    private MediaContent toDto(MediaEntity<?> entity, InputStream inputStream) {
        return new MediaContent()
                .setId(entity.getId())
                .setType(MediaType.parseMediaType(entity.getType()))
                .setContent(inputStream);
    }

    private Media toDto(MediaEntity<?> entity) {
        return new Media()
                .setId(entity.getId())
                .setType(MediaType.parseMediaType(entity.getType()));
    }
}
