package com.free.swd_392.dto.product.request;

import com.free.swd_392.core.model.IBaseData;
import com.free.swd_392.core.model.ICreateData;
import com.free.swd_392.dto.product.ProductConfigInfo;
import com.free.swd_392.enums.ProductStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CreateProductRequest implements ICreateData<Long> {

    @NotBlank
    @Size(max = 200)
    private String name;
    @Size(max = 16777215)
    private String description;
    @Size(max = 2083)
    private String image;
    @NotNull
    private ProductStatus status;
    @NotNull
    private Long categoryId;
    private List<@Valid ProductConfigInfo> productConfigs;
}
