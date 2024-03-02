package com.free.swd_392.config.minio;

public class MinioException extends RuntimeException {

    private MinioErrorCode errorCode;

    public MinioException() {
        super();
    }

    public MinioException(MinioErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = errorCode;
    }

    public MinioException(MinioErrorCode errorCode, Throwable cause) {
        super(errorCode.name(), cause);
        this.errorCode = errorCode;
    }

    public MinioErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(MinioErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
