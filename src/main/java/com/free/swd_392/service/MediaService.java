package com.free.swd_392.service;

import com.free.swd_392.config.minio.MinioService;
import com.free.swd_392.core.model.SuccessResponse;
import com.free.swd_392.dto.media.request.BulkUploadPreSignPrivateRequest;
import com.free.swd_392.dto.media.request.UploadRequest;
import com.free.swd_392.dto.media.request.UploadWithPreSignRequest;
import com.free.swd_392.dto.media.response.UploadWithPreSignResponse;
import com.free.swd_392.enums.MediaAccessType;
import com.free.swd_392.shared.model.media.*;
import com.free.swd_392.shared.utils.FileNameUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final MinioService minioService;

    public List<UploadWithPreSignResponse> bulkUploadPreSign(BulkUploadPreSignPrivateRequest request) {
        List<UploadWithPreSignResponse> listUrl = new ArrayList<>();
        for (var file : request.getFileMetadatas()) {
            FileInfo fileInfo = new FileInfo(file.getFileType(), FileNameUtils.generateFileName(), MediaAccessType.PRIVATE, file.getExtension());
            String preSignedUrl = minioService.generatePreSignedUrlUpload(fileInfo.getUploadPath());
            UploadWithPreSignResponse response = new UploadWithPreSignResponse(preSignedUrl, fileInfo.getPreviewUrl());
            listUrl.add(response);
        }
        return listUrl;
    }

    public SuccessResponse deleteFile(FilePath request) {
        FileInfo fileInfo = new FileInfo(request.getFileType(), request.getObjectName(), MediaAccessType.PRIVATE);
        minioService.remove(fileInfo.getUploadPath());
        return SuccessResponse.SUCCESS;
    }

    public PreSignUrl previewWithPreSigned(FilePath request) {
        FileInfo fileInfo = new FileInfo(request.getFileType(), request.getObjectName(), request.getMediaAccessType());
        return new PreSignUrl(minioService.generatePreSignedUrlDownload(fileInfo.getUploadPath()));
    }

    public FileDownloaded preview(FilePath request) {
        FileInfo fileInfo = new FileInfo(request.getFileType(), request.getObjectName(), request.getMediaAccessType());
        return minioService.get(fileInfo.getUploadPath());
    }

    public UploadWithPreSignResponse uploadWithPreSign(UploadWithPreSignRequest request) {
        FileInfo fileInfo = new FileInfo(request.getFileType(), FileNameUtils.generateFileName(), MediaAccessType.PRIVATE, request.getExtension());
        return new UploadWithPreSignResponse(
                minioService.generatePreSignedUrlUpload(fileInfo.getUploadPath()),
                fileInfo.getPreviewUrl()
        );
    }

    public UploadResult upload(UploadRequest request) {
        FileInfo fileInfo = new FileInfo(request.getFileType(), FileNameUtils.generateFileName(), request.getMediaAccessType(), request.getFile());
        minioService.upload(fileInfo);
        return new UploadResult(fileInfo);
    }

}
