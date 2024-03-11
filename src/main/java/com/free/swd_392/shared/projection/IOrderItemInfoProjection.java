package com.free.swd_392.shared.projection;

import com.free.swd_392.shared.model.order.OrderExtraVariant;

import java.math.BigDecimal;
import java.util.List;

/**
 * Projection for {@link com.free.swd_392.entity.order.OrderItemEntity}
 */
public interface IOrderItemInfoProjection {

    Long getId();

    String getProductName();

    BigDecimal getPrice();

    BigDecimal getDiscount();

    Integer getQuantity();

    List<OrderExtraVariant> getExtraVariants();

    String getNote();

    Long getProductId();
}
