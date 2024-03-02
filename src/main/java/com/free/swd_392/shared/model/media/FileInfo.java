package com.free.swd_392.shared.model.media;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.free.swd_392.enums.FileType;
import com.free.swd_392.enums.MediaAccessType;
import com.free.swd_392.exception.InvalidException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileInfo {

    private static final String SERVER_REWRITE_PATH;
    private static final String ENV;
    private static final boolean PREVIEW_DEFAULT_PRE_SIGNED;
    private static final String CONTROLLER_PATH = "/api/v1/media";

    static {
        if (System.getenv("SERVER_REWRITE_PATH") != null) {
            SERVER_REWRITE_PATH = System.getenv("SERVER_REWRITE_PATH");
        } else {
            SERVER_REWRITE_PATH = "";
        }
        String env = System.getenv("ENV");
        if (env == null || env.isEmpty()) {
            env = "dev";
        }
        if (env.toLowerCase().startsWith("prod")) {
            ENV = "production";
        } else if (env.toLowerCase().startsWith("stg") || env.toLowerCase().startsWith("staging")) {
            ENV = "stg";
        } else if (env.toLowerCase().startsWith("dev")) {
            ENV = "dev";
        } else {
            ENV = "local";
        }
        env = System.getenv("PREVIEW_DEFAULT_PRE_SIGNED");
        if (env != null) {
            PREVIEW_DEFAULT_PRE_SIGNED = "true".equals(env) || "True".equals(env);
        } else {
            PREVIEW_DEFAULT_PRE_SIGNED = false;
        }
    }

    @JsonIgnore
    private String path;
    @JsonIgnore
    private String name;
    private String extension;
    private String md5;
    private MediaAccessType mediaAccessType;
    @JsonIgnore
    private FileType fileType;
    @JsonIgnore
    private MultipartFile content;
    @JsonIgnore
    private MediaType contentTypeObject;

    public FileInfo(String fileType, String objectName, MediaAccessType mediaAccessType) {
        this(fileType, objectName, mediaAccessType, null);
    }

    public FileInfo(FileType fileType, String objectName, MediaAccessType mediaAccessType, String extension) {
        setFileType(fileType);
        setName(objectName);
        setMediaAccessType(mediaAccessType);
        setExtension(extension);
    }

    public FileInfo(String fileType, String objectName, MediaAccessType mediaAccessType, MultipartFile content) {
        this(FileType.valueFrom(fileType), objectName, mediaAccessType, content);
    }

    public FileInfo(FileType fileType, String objectName, MediaAccessType mediaAccessType, MultipartFile content) {
        if (fileType == null) {
            throw new InvalidException("MEDIA_FILE_NOT_FOUND");
        }
        setFileType(fileType);
        setName(objectName);
        setMediaAccessType(mediaAccessType);
        setContent(content);
    }

    @JsonIgnore
    public String getFullName() {
        if (StringUtils.isBlank(this.extension))
            return this.name;
        return String.format("%s.%s", this.name, this.extension);
    }

    public void setName(String name) {
        this.name = FilenameUtils.getBaseName(name);
        setExtension(FilenameUtils.getExtension(name));
    }

    public void setPath(String path) {
        if (!path.endsWith("/"))
            path = String.format("%s/", path);
        this.path = path.replaceFirst("^/", "");
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
        setPath(fileType.getPath());
    }

    @SneakyThrows
    public void setContent(MultipartFile content) {
        if (content == null) {
            return;
        }
        this.content = content;
        this.extension = FilenameUtils.getExtension(content.getOriginalFilename());
        this.contentTypeObject = MediaType.valueOf(Objects.requireNonNull(content.getContentType()));
    }

    @JsonProperty("contentType")
    public String getContentType() {
        if (this.contentTypeObject.equals(MediaType.ALL))
            return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        return MediaType.toString(List.of(this.contentTypeObject));
    }

    @JsonProperty("previewUrl")
    public String getPreviewUrl() {
        if (PREVIEW_DEFAULT_PRE_SIGNED) {
            return Path.of(SERVER_REWRITE_PATH, CONTROLLER_PATH, getMediaAccessType().getNameLowerCase(), "preview", "pre-sign", getFileType().getPath(), getFullName())
                    .toString();
        }
        return Path.of(SERVER_REWRITE_PATH, CONTROLLER_PATH, getMediaAccessType().getNameLowerCase(), "preview", getFileType().getPath(), getFullName())
                .toString();
    }

    @JsonIgnore
    public Path getUploadPath() {
        return Path.of(ENV, this.mediaAccessType.getNameLowerCase(), this.path, getFullName());
    }

    public static String getStoragePath(String realPath) {
        return realPath.replace(SERVER_REWRITE_PATH + CONTROLLER_PATH, "");
    }

}

