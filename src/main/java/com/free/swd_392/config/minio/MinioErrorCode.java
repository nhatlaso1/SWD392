package com.free.swd_392.config.minio;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MinioErrorCode {


    MINIO_STORAGE_ERROR(1, HttpStatus.INTERNAL_SERVER_ERROR),
    MINIO_FILE_FETCHING_ERROR(2, HttpStatus.INTERNAL_SERVER_ERROR),
    MINIO_FILE_METADATA_FETCHING_ERROR(3, HttpStatus.INTERNAL_SERVER_ERROR),
    MINIO_FILE_UPLOAD_FAILED(4, HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_DELETING_ERROR(5, HttpStatus.INTERNAL_SERVER_ERROR);
    private final int code;
    private final HttpStatus statusMapping;
}
