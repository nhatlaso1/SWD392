package com.free.swd_392.dto.product.request.filter;

import com.free.swd_392.core.model.IPageFilter;
import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PublicProductPageFilter implements IPageFilter {

    private Pageable pageable;
    private String name;
    private List<Long> categoryIds;
    private BigDecimal fromPrice;
    private BigDecimal toPrice;
}
