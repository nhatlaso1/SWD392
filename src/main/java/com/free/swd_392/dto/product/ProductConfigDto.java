package com.free.swd_392.dto.product;

import com.free.swd_392.enums.ProductConfigChoice;
import lombok.Data;

import java.util.List;

@Data
public class ProductConfigDto {

    private Long id;
    private String name;
    private ProductConfigChoice choiceKind;
    private boolean isRequired = false;
    private List<Object> variants;
}
