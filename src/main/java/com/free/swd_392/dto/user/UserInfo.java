package com.free.swd_392.dto.user;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.free.swd_392.core.model.IBaseData;
import lombok.Data;

@Data
public class UserInfo implements IBaseData<String> {

    private String id;
    private String name;
    private String email;
    private String phone;
    private String photo;
    @JsonSetter(nulls = Nulls.SKIP)
    private boolean active = true;
}
