package com.free.swd_392.dto.product;

import com.free.swd_392.dto.AuditInfo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class SkuInfo extends AuditInfo<UUID> {

    private UUID id;
    private BigDecimal price;
    private Integer quantity;
    private String image;
    private List<ProductVariantInfo> variants;
}
