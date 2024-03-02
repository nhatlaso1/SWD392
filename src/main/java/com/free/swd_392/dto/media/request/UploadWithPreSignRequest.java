package com.free.swd_392.dto.media.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.free.swd_392.enums.FileType;
import com.free.swd_392.enums.MediaAccessType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UploadWithPreSignRequest {

    @NotNull
    @Schema(example = "AVATAR")
    private FileType fileType;

    @Schema(example = "png")
    private String extension;

    @JsonIgnore
    private MediaAccessType mediaAccessType;

}
