package com.free.swd_392.dto.product;

import lombok.Data;

import java.util.List;

@Data
public class ProductDetails extends ProductInfo {

    private String description;
    private List<ProductConfigInfo> productConfigs;
}
