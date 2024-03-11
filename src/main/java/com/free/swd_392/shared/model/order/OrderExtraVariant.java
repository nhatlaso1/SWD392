package com.free.swd_392.shared.model.order;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class OrderExtraVariant {

    private String name;
    private String image;
    private List<String> variants;

    public void addVariant(String variantName) {
        if(variants == null) {
            variants = new LinkedList<>();
        }
        variants.add(variantName);
    }
}
