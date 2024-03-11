package com.free.swd_392.dto.order.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateOrderItemRequest {

    @NotNull
    private UUID skuId;
    @NotNull
    @Min(1)
    private Integer quantity;
    @Column(length = 200)
    private String note;
}
