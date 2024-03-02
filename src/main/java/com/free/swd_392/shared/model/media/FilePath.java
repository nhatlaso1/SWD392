package com.free.swd_392.shared.model.media;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.free.swd_392.enums.MediaAccessType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilePath {

    @NotBlank
    protected String fileType;

    @NotBlank
    protected String objectName;

    @JsonIgnore
    protected MediaAccessType mediaAccessType;

    protected FilePath(String fileType, String objectName) {
        this.fileType = fileType;
        this.objectName = objectName;
    }
}
