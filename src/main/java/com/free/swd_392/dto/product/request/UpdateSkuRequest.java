package com.free.swd_392.dto.product.request;

import com.free.swd_392.core.model.IBaseData;
import com.free.swd_392.dto.product.SkuConfigInfo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class UpdateSkuRequest implements IBaseData<UUID> {

    @NotNull
    private UUID id;
    @Min(0)
    private BigDecimal price;
    @Min(0)
    private Integer quantity;
    private String image;
    List<SkuConfigInfo> configs;
}
