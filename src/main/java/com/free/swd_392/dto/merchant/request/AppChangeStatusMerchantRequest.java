package com.free.swd_392.dto.merchant.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.free.swd_392.core.model.IBaseData;
import com.free.swd_392.enums.MerchantStatus;
import com.free.swd_392.shared.validation.MerchantStatusValidator;
import lombok.Data;

@Data
public class AppChangeStatusMerchantRequest implements IBaseData<Long> {

    @JsonIgnore
    private Long id;
    @MerchantStatusValidator({MerchantStatus.INACTIVE, MerchantStatus.ACTIVE})
    private MerchantStatus status;
}
