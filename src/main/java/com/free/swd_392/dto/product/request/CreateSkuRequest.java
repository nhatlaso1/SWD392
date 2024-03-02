package com.free.swd_392.dto.product.request;

import com.free.swd_392.core.model.ICreateData;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Data
public class CreateSkuRequest implements ICreateData<UUID> {

    @Min(0)
    private BigDecimal price;
    @Min(0)
    private Integer quantity;
    private String image;
    @NotNull
    private Long productId;
    private Set<Long> variantIds;

}
