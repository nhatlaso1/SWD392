package com.free.swd_392.controller;

import com.free.swd_392.core.model.BaseResponse;
import com.free.swd_392.dto.media.request.UploadRequest;
import com.free.swd_392.enums.MediaAccessType;
import com.free.swd_392.service.MediaService;
import com.free.swd_392.shared.model.media.FileDownloaded;
import com.free.swd_392.shared.model.media.FilePath;
import com.free.swd_392.shared.model.media.PreSignUrl;
import com.free.swd_392.shared.model.media.UploadResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;

@Validated
@Tag(name = "Media Controller", description = "Upload & download file")
@RestController
@RequestMapping(value = "/api/v1/media")
@RequiredArgsConstructor
public class MediaController extends BaseController {

    private final MediaService mediaFactory;

    /*
    @Operation(summary = "Upload file")
    @PostMapping(value = "/private/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse<UploadResult>> uploadPrivate(@Valid @ModelAttribute UploadRequest request) {
        request.setMediaAccessType(MediaAccessType.PRIVATE);
        return wrapResponse(() -> mediaFactory.upload(request));
    }

    @Operation(summary = "Request PreSigned URL Upload file")
    @GetMapping(value = "/private/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<BaseResponse<UploadWithPreSignResponse>> uploadPreSignPrivate(@Valid @ParameterObject UploadWithPreSignRequest request) {
        request.setMediaAccessType(MediaAccessType.PRIVATE);
        return wrapResponse(() -> mediaFactory.uploadWithPreSign(request));
    }

    @Operation(summary = "Request PreSigned URL Upload file in bulk")
    @PostMapping(value = "/private/upload/bulk")
    ResponseEntity<BaseResponse<List<UploadWithPreSignResponse>>> bulkUploadPreSignPrivate(@Valid @RequestBody BulkUploadPreSignPrivateRequest request) {
        return wrapResponse(() -> mediaFactory.bulkUploadPreSign(request));
    }

    @Operation(summary = "Download file")
    @GetMapping(value = "/private/preview/{fileType}/{objectName}")
    ResponseEntity<StreamingResponseBody> previewPrivate(@NotBlank @PathVariable @Schema(example = "avatars") String fileType,
                                                         @NotBlank @PathVariable String objectName) {
        FilePath filePath = new FilePath(fileType, objectName, MediaAccessType.PRIVATE);
        return preview(filePath);
    }

    @Operation(
            summary = "Preview file private",
            description = "Preview file private, redirect to AWS S3 with pre-signed URL"
    )
    @GetMapping(value = "/private/preview/pre-sign/{fileType}/{objectName}")
    void previewPreSignPrivate(@NotBlank @PathVariable @Schema(example = "avatars") String fileType,
                               @NotBlank @PathVariable String objectName,
                               HttpServletResponse context) throws IOException {
        FilePath filePath = new FilePath(fileType, objectName, MediaAccessType.PRIVATE);
        previewPreSign(filePath, context);
    }

    @Operation(
            summary = "Get pre-sign url as string"
    )
    @GetMapping(value = "/private/preview/pre-sign-url/{fileType}/{objectName}")
    ResponseEntity<BaseResponse<PreSignUrl>> getPreSignDownloadPrivate(@NotBlank @PathVariable @Schema(example = "avatars") String fileType,
                                                                       @NotBlank @PathVariable String objectName) {
        FilePath filePath = new FilePath(fileType, objectName, MediaAccessType.PRIVATE);
        return wrapResponse(() -> mediaFactory.previewWithPreSigned(filePath));
    }

    @Operation(summary = "Delete file")
    @DeleteMapping(value = "/private/delete/{fileType}/{objectName}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<BaseResponse<SuccessResponse>> deletePrivate(@NotBlank @PathVariable @Schema(example = "avatars") String fileType,
                                                                @NotBlank @PathVariable String objectName) {
        FilePath filePath = new FilePath(fileType, objectName, MediaAccessType.PRIVATE);
        return wrapResponse(() -> mediaFactory.deleteFile(filePath));
    }*/

    @Operation(
            summary = "Upload file public",
            description = "Upload file public with file type"
    )
    @PostMapping(value = "/public/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<BaseResponse<UploadResult>> uploadPublic(@Valid @ModelAttribute UploadRequest request) {
        request.setMediaAccessType(MediaAccessType.PUBLIC);
        return wrapResponse(() -> mediaFactory.upload(request));
    }

    @Operation(
            summary = "Preview file public",
            description = "Preview file public, redirect to AWS S3 with pre-signed URL"
    )
    @GetMapping(value = "/public/preview/{fileType}/{objectName}")
    ResponseEntity<StreamingResponseBody> previewPubic(@NotBlank @PathVariable @Schema(example = "avatars") String fileType,
                                                       @NotBlank @PathVariable String objectName,
                                                       HttpServletResponse context) {
        FilePath filePath = new FilePath(fileType, objectName, MediaAccessType.PUBLIC);
        return preview(filePath);
    }

    @Operation(
            summary = "Preview file public",
            description = "Preview file public, redirect to AWS S3 with pre-signed URL"
    )
    @GetMapping(value = "/public/preview/pre-sign/{fileType}/{objectName}")
    void previewPreSignPublic(@NotBlank @PathVariable @Schema(example = "avatars") String fileType,
                              @NotBlank @PathVariable String objectName,
                              HttpServletResponse context) throws IOException {
        FilePath filePath = new FilePath(fileType, objectName, MediaAccessType.PUBLIC);
        previewPreSign(filePath, context);
    }

    @Operation(
            summary = "Get pre-sign url as string"
    )
    @GetMapping(value = "/public/preview/pre-sign-url/{fileType}/{objectName}")
    ResponseEntity<BaseResponse<PreSignUrl>> getPreSignDownloadPublic(@NotBlank @PathVariable @Schema(example = "avatars") String fileType,
                                                                      @NotBlank @PathVariable String objectName) {
        FilePath filePath = new FilePath(fileType, objectName, MediaAccessType.PUBLIC);
        return wrapResponse(() -> mediaFactory.previewWithPreSigned(filePath));
    }

    private ResponseEntity<StreamingResponseBody> preview(FilePath request) {
        FileDownloaded response = mediaFactory.preview(request);
        var resultBuilder = ResponseEntity.ok()
                .contentType(response.getContentType());
        if (!response.getContentType().getType().startsWith("image")) {
            resultBuilder.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + response.getFileName());
        }
        StreamingResponseBody streamingResponseBody = out -> IOUtils.copy(response.getInputStream(), out);
        return resultBuilder.body(streamingResponseBody);
    }

    private void previewPreSign(FilePath request, HttpServletResponse context) throws IOException {
        PreSignUrl response = mediaFactory.previewWithPreSigned(request);
        if (response.getUrl() != null) {
            context.sendRedirect(response.getUrl());
        }
    }
}
