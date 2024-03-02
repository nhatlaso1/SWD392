package com.free.swd_392.dto.user.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.free.swd_392.core.model.IBaseData;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import static com.fasterxml.jackson.annotation.Nulls.SKIP;

@Data
public class UpdateUserAddressRequest implements IBaseData<Long> {

    @NotNull
    private Long id;
    private String addressDetails;
    private String receiverFullName;
    private String phone;
    @JsonSetter(nulls = SKIP)
    private boolean isDefault = false;
    private String note;
    @NotNull
    private Long provinceId;
    @NotNull
    private Long districtId;
    @NotNull
    private Long wardId;
}
