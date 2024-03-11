package com.free.swd_392.dto.merchant;

import com.free.swd_392.core.model.IBaseData;
import com.free.swd_392.enums.MerchantStatus;
import lombok.Data;

@Data
public class MerchantInfo implements IBaseData<Long> {

    private Long id;
    private String name;
    private String phone;
    private String avatar;
    private String coverImage;
    private String description;
    private Long provinceId;
    private String provinceName;
    private Long districtId;
    private String districtName;
    private Long wardId;
    private String wardName;
    private String addressDetails;
    private String addressNote;
    private MerchantStatus status;
    private String representativeName;

    private String facebook;
    private String zalo;
    private String instagram;
    private String youtube;
}
