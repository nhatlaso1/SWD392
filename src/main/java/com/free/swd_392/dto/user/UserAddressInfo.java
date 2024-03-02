package com.free.swd_392.dto.user;

import com.free.swd_392.core.model.IBaseData;
import lombok.Data;

@Data
public class UserAddressInfo implements IBaseData<Long> {

    private Long id;
    private String addressDetails;
    private String receiverFullName;
    private String phone;
    private boolean isDefault = false;
    private String note;
    private Long provinceId;
    private String provinceName;
    private Long districtId;
    private String districtName;
    private Long wardId;
    private String wardName;
}
