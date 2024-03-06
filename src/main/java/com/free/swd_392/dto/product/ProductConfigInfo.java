package com.free.swd_392.dto.product;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.free.swd_392.core.model.IBaseData;
import com.free.swd_392.enums.ProductConfigChoice;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class ProductConfigInfo implements IBaseData<Long> {

    private Long id;
    @NotBlank
    @Size(max = 200)
    private String name;
    @JsonSetter(nulls = Nulls.SKIP)
    private ProductConfigChoice choiceKind = ProductConfigChoice.SINGLE_CHOICE;
    private boolean isRequired = false;
    @NotEmpty
    private List<@Valid ProductVariantInfo> variants;
}
