package com.free.swd_392.dto.product;

import com.free.swd_392.core.model.IBaseData;
import com.free.swd_392.enums.ProductStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ProductInfo implements IBaseData<Long> {

    private Long id;
    private String name;
    private String image;
    private ProductStatus status;
    private Long categoryId;
    private String categoryName;
    private BigDecimal fromPrice;
    private BigDecimal toPrice;
    private Long merchantId;

    public ProductInfo(Long id,
                       String name,
                       String image,
                       ProductStatus status,
                       Long categoryId,
                       String categoryName,
                       BigDecimal fromPrice,
                       BigDecimal toPrice) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.status = status;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.fromPrice = fromPrice;
        this.toPrice = toPrice;
    }
}
