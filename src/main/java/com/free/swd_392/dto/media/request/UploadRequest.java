package com.free.swd_392.dto.media.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.free.swd_392.enums.FileType;
import com.free.swd_392.enums.MediaAccessType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadRequest {

    @NotNull
    @Schema(example = "AVATAR")
    private FileType fileType;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private MultipartFile file;

    @JsonIgnore
    private MediaAccessType mediaAccessType;
}
