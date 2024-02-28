package com.free.swd_392.dto.product;

import com.free.swd_392.core.model.IBaseData;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductVariantInfo implements IBaseData<Long> {

    private Long id;
    @NotBlank
    @Size(max = 200)
    private String name;
    @Min(0)
    private Integer ordering;
    private Boolean isSoldOut;
}
