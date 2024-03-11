package com.free.swd_392.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.free.swd_392.dto.AuditInfo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SkuInfo extends AuditInfo<UUID> {

    private UUID id;
    private BigDecimal price;
    private Integer quantity;
    private String image;
    private List<Long> variantIds;
    private List<ProductVariantInfo> variants;
}
