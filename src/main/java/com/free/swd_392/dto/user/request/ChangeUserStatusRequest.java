package com.free.swd_392.dto.user.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangeUserStatusRequest {

    @NotBlank
    private String userId;
    @JsonSetter(nulls = Nulls.SKIP)
    private boolean active = true;
}
