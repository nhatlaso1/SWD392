package com.free.swd_392.dto.user;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.free.swd_392.core.model.IBaseData;
import com.free.swd_392.enums.RoleKind;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class UserInfo implements IBaseData<String> {

    private String id;
    @NotBlank
    @Size(max = 50)
    private String name;
    @Email
    private String email;
    @NotBlank
    @Size(max = 10)
    private String phone;
    @Size(max = 2083)
    private String photo;
    @JsonSetter(nulls = Nulls.SKIP)
    private boolean active = true;
    private UUID roleId;
    private RoleKind roleKind;
}
