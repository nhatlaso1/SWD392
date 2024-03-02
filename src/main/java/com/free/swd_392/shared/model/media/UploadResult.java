package com.free.swd_392.shared.model.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadResult {

    private String contentType;
    private String extension;
    private String md5;
    private String previewUrl;

    public UploadResult(FileInfo fileInfo) {
        this.contentType = fileInfo.getContentType();
        this.extension = fileInfo.getExtension();
        this.md5 = fileInfo.getMd5();
        this.previewUrl = fileInfo.getPreviewUrl();
    }
}
