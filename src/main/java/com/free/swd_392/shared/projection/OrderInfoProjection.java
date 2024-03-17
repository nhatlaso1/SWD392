package com.free.swd_392.shared.projection;

import com.fasterxml.jackson.core.type.TypeReference;
import com.free.swd_392.shared.model.order.OrderExtraVariant;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * Projection for {@link com.free.swd_392.entity.order.OrderEntity}
 */
public interface OrderInfoProjection extends MerchantOrderInfoProjection {

    @Value("#{new com.free.swd_392.shared.projection.OrderItemInfoProjection(target.id, target.productName, target.price, target.discount, target.quantity, @jacksonObjectMapper.readValue(target.extraVariants, T(com.free.swd_392.shared.projection.OrderInfoProjection).EXTRAS_TYPE_REFERENCE), target.note, target.productId)}")
    IOrderItemInfoProjection getFirstItem();

    TypeReference<List<OrderExtraVariant>> EXTRAS_TYPE_REFERENCE = new TypeReference<>() {
    };

}
