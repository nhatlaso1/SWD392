package com.free.swd_392.dto.merchant.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.free.swd_392.core.model.ICreateData;
import com.free.swd_392.enums.MerchantStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AppCreateMerchantRequest implements ICreateData<Long> {

    @NotBlank
    @Size(max = 50)
    private String name;
    @NotBlank
    @Size(max = 10)
    private String phone;
    @Size(max = 2083)
    private String avatar;
    @Size(max = 2083)
    private String coverImage;
    @Size(max = 16777215)
    private String description;
    @NotNull
    private Long provinceId;
    @NotNull
    private Long districtId;
    @NotNull
    private Long wardId;
    @NotBlank
    private String addressDetails;
    @Size(max = 1000)
    private String addressNote;

    @NotBlank
    @Size(max = 50)
    private String representativeName;
    @NotBlank
    @Email
    @Size(max = 50)
    private String representativeEmail;
    @NotBlank
    @Size(max = 10)
    private String representativePhone;
    @NotBlank
    @Size(max = 2083)
    private String idFront;
    @NotBlank
    @Size(max = 2083)
    private String idBack;

    @Size(max = 2083)
    private String facebook;
    @Size(max = 2083)
    private String zalo;
    @Size(max = 2083)
    private String instagram;
    @Size(max = 2083)
    private String youtube;

    @JsonIgnore
    private String ownerId;
    @JsonIgnore
    private MerchantStatus status = MerchantStatus.DRAFT;
}
