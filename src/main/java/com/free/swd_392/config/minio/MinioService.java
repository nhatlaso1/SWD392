package com.free.swd_392.config.minio;

import com.free.swd_392.shared.model.media.FileDownloaded;
import com.free.swd_392.shared.model.media.FileInfo;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


/**
 * Service class to interact with Minio bucket. This class is register as a bean and use the properties defined in {@link MinioConfigurationProperties}.
 * All methods return an {@link MinioException} which wrap the Minio SDK exception.
 * The bucket name is provided with the one defined in the configuration properties.
 *
 * @author Jordan LEFEBURE
 * <p>
 * <p>
 * This service adapetd with minio sdk 7.0.x
 * @author Mostafa Jalambadani
 */

@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings({"unused"})
public class MinioService {

    public static final String GOT_MINIO_EXCEPTION = "Got Minio exception";
    private final MinioClient minioClient;
    private final MinioConfigurationProperties configurationProperties;
    @Value("${spring.minio.pre-signed-upload-duration}")
    private final int preSignedUrlDuration;

    /**
     * List all objects at root of the bucket
     *
     * @return List of items
     */
    public List<Item> list() {
        ListObjectsArgs args = ListObjectsArgs.builder()
                .bucket(configurationProperties.getBucket())
                .prefix("")
                .recursive(false)
                .build();
        Iterable<Result<Item>> myObjects = minioClient.listObjects(args);
        return getItems(myObjects);
    }

    /**
     * List all objects at root of the bucket
     *
     * @return List of items
     */
    public List<Item> fullList() {
        ListObjectsArgs args = ListObjectsArgs.builder()
                .bucket(configurationProperties.getBucket())
                .build();
        Iterable<Result<Item>> myObjects = minioClient.listObjects(args);
        return getItems(myObjects);
    }

    /**
     * List all objects with the prefix given in parameter for the bucket.
     * Simulate a folder hierarchy. Objects within folders (i.e. all objects which match the pattern {@code {prefix}/{objectName}/...}) are not returned
     *
     * @param path Prefix of seeked list of object
     * @return List of items
     */
    public List<Item> list(Path path) {
        ListObjectsArgs args = ListObjectsArgs.builder()
                .bucket(configurationProperties.getBucket())
                .prefix(path.toString())
                .recursive(false)
                .build();
        Iterable<Result<Item>> myObjects = minioClient.listObjects(args);
        return getItems(myObjects);
    }

    /**
     * List all objects with the prefix given in parameter for the bucket
     * <p>
     * All objects, even those which are in a folder are returned.
     *
     * @param path Prefix of seeked list of object
     * @return List of items
     */
    public List<Item> getFullList(Path path) {
        ListObjectsArgs args = ListObjectsArgs.builder()
                .bucket(configurationProperties.getBucket())
                .prefix(path.toString())
                .build();
        Iterable<Result<Item>> myObjects = minioClient.listObjects(args);
        return getItems(myObjects);
    }

    /**
     * Utility method which map results to items and return a list
     *
     * @param myObjects Iterable of results
     * @return List of items
     */
    private List<Item> getItems(Iterable<Result<Item>> myObjects) {
        return StreamSupport
                .stream(myObjects.spliterator(), true)
                .map(itemResult -> {
                    try {
                        return itemResult.get();
                    } catch (Exception e) {
                        log.error(GOT_MINIO_EXCEPTION, e);
                        throw new MinioException(MinioErrorCode.MINIO_STORAGE_ERROR);
                    }
                })
                .toList();
    }

    /**
     * Get an object from Minio
     *
     * @param path Path with prefix to the object. Object name must be included.
     * @return The object as an InputStream
     * @throws MinioException if an error occur while fetch object
     */
    public FileDownloaded get(Path path) {
        try {
            GetObjectArgs args = GetObjectArgs.builder()
                    .bucket(configurationProperties.getBucket())
                    .object(path.toString())
                    .build();
            GetObjectResponse response = minioClient.getObject(args);
            MediaType contentType = MediaType.APPLICATION_OCTET_STREAM;
            if (response.headers().get("Content-Type") != null) {
                contentType = MediaType.valueOf(Objects.requireNonNull(response.headers().get("Content-Type")));
            }
            return new FileDownloaded(response, response.object(), contentType);
        } catch (Exception e) {
            log.error(GOT_MINIO_EXCEPTION, e);
            throw new MinioException(MinioErrorCode.MINIO_FILE_FETCHING_ERROR);
        }
    }

    /**
     * Get metadata of an object from Minio
     *
     * @param path Path with prefix to the object. Object name must be included.
     * @return Metadata of the  object
     * @throws MinioException if an error occur while fetching object metadatas
     */
    public StatObjectResponse getMetadata(Path path) {
        try {
            StatObjectArgs args = StatObjectArgs.builder()
                    .bucket(configurationProperties.getBucket())
                    .object(path.toString())
                    .build();
            return minioClient.statObject(args);
        } catch (Exception e) {
            log.error(GOT_MINIO_EXCEPTION, e);
            throw new MinioException(MinioErrorCode.MINIO_FILE_METADATA_FETCHING_ERROR);
        }
    }

    /**
     * Get metadata for multiples objects from Minio
     *
     * @param paths Paths of all objects with prefix. Objects names must be included.
     * @return A map where all paths are keys and metadatas are values
     */
    public Map<Path, StatObjectResponse> getMetadata(Iterable<Path> paths) {
        return StreamSupport.stream(paths.spliterator(), false)
                .map(path -> {
                    try {
                        StatObjectArgs args = StatObjectArgs.builder()
                                .bucket(configurationProperties.getBucket())
                                .object(path.toString())
                                .build();
                        return new HashMap.SimpleEntry<>(path, minioClient.statObject(args));
                    } catch (Exception e) {
                        log.error(GOT_MINIO_EXCEPTION, e);
                        throw new MinioException(MinioErrorCode.MINIO_STORAGE_ERROR);
                    }
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Get a file from Minio, and save it in the {@code fileName} file
     *
     * @param source   Path with prefix to the object. Object name must be included.
     * @param fileName Filename
     * @throws MinioException if an error occur while fetch object
     */
    public void getAndSave(Path source, String fileName) {
        try {
            DownloadObjectArgs args = DownloadObjectArgs.builder()
                    .bucket(configurationProperties.getBucket())
                    .object(source.toString())
                    .filename(fileName)
                    .build();
            minioClient.downloadObject(args);
        } catch (Exception e) {
            log.error(GOT_MINIO_EXCEPTION, e);
            throw new MinioException(MinioErrorCode.MINIO_FILE_FETCHING_ERROR);
        }
    }

    /**
     * Upload a file to Minio
     *
     * @param source  Path with prefix to the object. Object name must be included.
     * @param file    File as an input stream
     * @param headers Additional headers to put on the file. The map MUST be mutable. All custom headers will start with 'x-amz-meta-' prefix when fetched with {@code getMetadata()} method.
     * @throws MinioException if an error occur while uploading object
     */
    public void upload(Path source, InputStream file, Map<String, String> headers) {
        try {
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(configurationProperties.getBucket())
                    .object(source.toString())
                    .stream(file, file.available(), -1)
                    .headers(headers)
                    .build();
            minioClient.putObject(args);
        } catch (Exception e) {
            log.error(GOT_MINIO_EXCEPTION, e);
            throw new MinioException(MinioErrorCode.MINIO_FILE_UPLOAD_FAILED);
        }
    }

    /**
     * Upload a file to Minio
     *
     * @param source Path with prefix to the object. Object name must be included.
     * @param file   File as an input stream
     * @throws MinioException if an error occur while uploading object
     */
    public void upload(Path source, InputStream file) {
        try {
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(configurationProperties.getBucket())
                    .object(source.toString())
                    .stream(file, file.available(), -1)
                    .build();
            minioClient.putObject(args);
        } catch (Exception e) {
            log.error(GOT_MINIO_EXCEPTION, e);
            throw new MinioException(MinioErrorCode.MINIO_FILE_UPLOAD_FAILED);
        }
    }

    /**
     * Upload a file to Minio
     *
     * @param source      Path with prefix to the object. Object name must be included.
     * @param file        File as an input stream
     * @param contentType MIME type for the object
     * @param headers     Additional headers to put on the file. The map MUST be mutable
     * @throws MinioException if an error occur while uploading object
     */
    public void upload(Path source, InputStream file, String contentType, Map<String, String> headers) {
        try {
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(configurationProperties.getBucket())
                    .object(source.toString())
                    .stream(file, file.available(), -1)
                    .headers(headers)
                    .contentType(contentType)
                    .build();

            minioClient.putObject(args);
        } catch (Exception e) {
            log.error(GOT_MINIO_EXCEPTION, e);
            throw new MinioException(MinioErrorCode.MINIO_FILE_UPLOAD_FAILED);
        }
    }

    /**
     * Upload a file to Minio
     *
     * @param source      Path with prefix to the object. Object name must be included.
     * @param file        File as an input stream
     * @param contentType MIME type for the object
     * @throws MinioException if an error occur while uploading object
     */
    public void upload(Path source, InputStream file, String contentType) {
        try {
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(configurationProperties.getBucket())
                    .object(source.toString())
                    .stream(file, file.available(), -1)
                    .contentType(contentType)
                    .build();

            minioClient.putObject(args);
        } catch (Exception e) {
            log.error(GOT_MINIO_EXCEPTION, e);
            throw new MinioException(MinioErrorCode.MINIO_FILE_UPLOAD_FAILED);
        }
    }

    /**
     * Upload a file to Minio
     * upload file bigger than Xmx size
     *
     * @param source Path with prefix to the object. Object name must be included.
     * @param file   File as a Filename
     * @throws MinioException if an error occur while uploading object
     */
    public void upload(Path source, File file) {
        try {
            UploadObjectArgs args = UploadObjectArgs.builder()
                    .bucket(configurationProperties.getBucket())
                    .object(source.toString())
                    .filename(file.getAbsolutePath())
                    .build();
            minioClient.uploadObject(args);
        } catch (Exception e) {
            log.error(GOT_MINIO_EXCEPTION, e);
            throw new MinioException(MinioErrorCode.MINIO_FILE_UPLOAD_FAILED);
        }
    }

    public void upload(FileInfo fileInfo) {
        try {
            upload(fileInfo.getUploadPath(), fileInfo.getContent().getInputStream(), fileInfo.getContentType());
        } catch (IOException e) {
            log.error(GOT_MINIO_EXCEPTION, e);
            throw new MinioException(MinioErrorCode.MINIO_FILE_UPLOAD_FAILED);
        }
    }


    /**
     * Remove a file to Minio
     *
     * @param source Path with prefix to the object. Object name must be included.
     * @throws MinioException if an error occur while removing object
     */
    public void remove(Path source) {
        try {
            RemoveObjectArgs args = RemoveObjectArgs.builder()
                    .bucket(configurationProperties.getBucket())
                    .object(source.toString())
                    .build();
            minioClient.removeObject(args);
        } catch (Exception e) {
            log.error(GOT_MINIO_EXCEPTION, e);
            throw new MinioException(MinioErrorCode.FILE_DELETING_ERROR);
        }
    }

    public String generatePreSignedUrlDownload(Path source) {
        GetPresignedObjectUrlArgs getPresignedObjectUrlArgs = GetPresignedObjectUrlArgs.builder()
                .expiry(preSignedUrlDuration)
                .method(Method.GET)
                .bucket(configurationProperties.getBucket())
                .object(source.toString())
                .build();
        try {
            return minioClient.getPresignedObjectUrl(getPresignedObjectUrlArgs);
        } catch (Exception e) {
            log.error(GOT_MINIO_EXCEPTION, e);
            throw new MinioException(MinioErrorCode.MINIO_FILE_METADATA_FETCHING_ERROR);
        }
    }

    public String generatePreSignedUrlUpload(Path source) {
        GetPresignedObjectUrlArgs getPresignedObjectUrlArgs = GetPresignedObjectUrlArgs.builder()
                .expiry(preSignedUrlDuration)
                .method(Method.PUT)
                .bucket(configurationProperties.getBucket())
                .object(source.toString())
                .build();
        try {
            return minioClient.getPresignedObjectUrl(getPresignedObjectUrlArgs);
        } catch (Exception e) {
            log.error(GOT_MINIO_EXCEPTION, e);
            throw new MinioException(MinioErrorCode.MINIO_FILE_METADATA_FETCHING_ERROR);
        }
    }

}
