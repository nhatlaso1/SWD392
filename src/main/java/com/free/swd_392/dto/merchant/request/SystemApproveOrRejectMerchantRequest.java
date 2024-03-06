package com.free.swd_392.dto.merchant.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SystemApproveOrRejectMerchantRequest {

    @NotNull
    private Long merchantId;
    @NotNull
    private Boolean approve;
    @NotBlank
    @Size(max = 1000)
    private String reason;
}
