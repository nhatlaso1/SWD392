package com.free.swd_392.dto.merchant.request;

import com.free.swd_392.enums.MerchantStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SystemApproveOrRejectMerchantRequest {

    @NotNull
    private Long merchantId;
    @NotNull
    private MerchantStatus status;
    @Size(max = 1000)
    private String reason;
}
