package com.free.swd_392.dto.product;

import com.free.swd_392.core.model.IBaseData;
import com.free.swd_392.enums.ProductStatus;
import lombok.Data;

@Data
public class ProductInfo implements IBaseData<Long> {

    private Long id;
    private String name;
    private String image;
    private ProductStatus status;
    private Long categoryId;
    private String categoryName;
}
