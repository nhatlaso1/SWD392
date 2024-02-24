package com.free.swd_392.dto.product.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeOrderProductCategoryRequest {

    @NotNull
    private Long id;
    @NotNull
    private Integer newOrder;
}
