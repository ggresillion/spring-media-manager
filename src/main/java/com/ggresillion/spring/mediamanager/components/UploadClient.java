package com.ggresillion.spring.mediamanager.components;

import com.ggresillion.spring.mediamanager.exceptions.MediaException;
import com.ggresillion.spring.mediamanager.repositories.MediaRepository;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.*;
import io.minio.messages.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Component
public class UploadClient {

    Logger logger = LoggerFactory.getLogger(UploadClient.class);

    private static final String BUCKET = "default";

    @Value("${minio.host}")
    private String minioHost;

    @Value("${minio.accessKey}")
    private String minioAccessKey;

    @Value("${minio.secretKey}")
    private String minioSecretKey;

    private MinioClient client;

    @Autowired
    private MediaRepository mediaRepository;

    @PostConstruct
    private void init()
            throws InvalidPortException,
            InvalidEndpointException,
            IOException,
            InvalidKeyException,
            NoSuchAlgorithmException,
            InsufficientDataException,
            InvalidResponseException,
            InternalException,
            NoResponseException,
            InvalidBucketNameException,
            XmlPullParserException,
            ErrorResponseException,
            RegionConflictException {
        logger.info("MINIO: connecting to {}", minioHost);
        MinioClient c = new MinioClient(minioHost, minioAccessKey, minioSecretKey);
        if (!c.bucketExists(BUCKET)) {
            c.makeBucket(BUCKET);
        }
        this.client = c;
    }

    /**
     * Upload a new file to the bucket and return an UUID
     *
     * @param file file to upload
     * @return uuid
     * @throws MediaException error with the upload API
     */
    public UUID uploadFile(MultipartFile file) throws MediaException {
        UUID uuid = UUID.randomUUID();
        try {
            client.putObject(
                    BUCKET,
                    uuid.toString(),
                    file.getInputStream(),
                    Long.valueOf(file.getInputStream().available()),
                    null,
                    null,
                    file.getContentType());
        } catch (InvalidKeyException | InsufficientDataException | NoSuchAlgorithmException | NoResponseException |
                InvalidResponseException | XmlPullParserException | InvalidArgumentException |
                InvalidBucketNameException | ErrorResponseException | InternalException | IOException e) {
            throw new MediaException();
        }
        return uuid;
    }

    /**
     * Upload a new file to the bucket and return an UUID
     *
     * @param file file to upload
     * @return uuid
     * @throws MediaException error with the upload API
     */
    public UUID uploadFile(InputStream file, String type) throws MediaException {
        UUID uuid = UUID.randomUUID();
        try {
            client.putObject(
                    BUCKET,
                    uuid.toString(),
                    file,
                    Long.valueOf(file.available()),
                    null,
                    null,
                    type);
        } catch (Exception e) {
            throw new MediaException();
        }
        return uuid;
    }

    /**
     * Get a file by UUID
     *
     * @param id uuid
     * @return file
     * @throws MediaException error with the upload API
     */
    public InputStream getFile(UUID id) throws MediaException {
        try {
            return client.getObject(BUCKET, id.toString());
        } catch (Exception e) {
            throw new MediaException();
        }
    }

    /**
     * Delete a file by uuid
     *
     * @param id uuid
     * @throws MediaException error with the upload API
     */
    public void deleteFile(UUID id) throws MediaException {
        try {
            client.removeObject(BUCKET, id.toString());
        } catch (Exception e) {
            throw new MediaException();
        }
    }
}
