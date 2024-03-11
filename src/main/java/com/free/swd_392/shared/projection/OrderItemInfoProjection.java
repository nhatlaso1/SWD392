package com.free.swd_392.shared.projection;

import com.free.swd_392.shared.model.order.OrderExtraVariant;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderItemInfoProjection implements IOrderItemInfoProjection {

    private Long id;
    private String productName;
    private BigDecimal price;
    private BigDecimal discount;
    private Integer quantity;
    private List<OrderExtraVariant> extraVariants;
    private String note;
    private Long productId;
}
