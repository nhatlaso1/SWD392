package com.free.swd_392.dto.order;

import com.free.swd_392.core.model.IBaseData;
import com.free.swd_392.shared.model.order.OrderExtraVariant;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderItemInfo implements IBaseData<Long> {

    private Long id;
    private String productName;
    private BigDecimal price;
    private BigDecimal discount;
    private Integer quantity;
    private List<OrderExtraVariant> extraVariants;
    private String note;
    private Long productId;
}
