package com.free.swd_392.shared.projection;

import com.fasterxml.jackson.core.type.TypeReference;
import com.free.swd_392.enums.OrderStatus;
import com.free.swd_392.enums.order.PaymentMethodProvider;
import com.free.swd_392.shared.model.order.OrderExtraVariant;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Projection for {@link com.free.swd_392.entity.order.OrderEntity}
 */
public interface OrderInfoProjection {
    String getCreatedBy();

    LocalDateTime getCreatedDate();

    String getLastModifiedBy();

    LocalDateTime getLastModifiedDate();

    default UUID getId() {
        ByteBuffer buf = ByteBuffer.wrap(getuid());
        return new UUID(buf.getLong(), buf.getLong());
    }

    byte[] getuid();

    String getReceiverFullName();

    String getPhone();

    String getProvinceName();

    String getDistrictName();

    String getWardName();

    String getAddressDetails();

    BigDecimal getSubTotal();

    BigDecimal getShippingCharge();

    BigDecimal getDiscount();

    String getDescription();

    PaymentMethodProvider getPaymentMethod();

    OrderStatus getStatus();

    Integer getNumItem();

    @Value("#{new com.free.swd_392.shared.projection.OrderItemInfoProjection(target.id, target.productName, target.price, target.discount, target.quantity, @jacksonObjectMapper.readValue(target.extraVariants, T(com.free.swd_392.shared.projection.OrderInfoProjection).EXTRAS_TYPE_REFERENCE), target.note, target.productId)}")
    IOrderItemInfoProjection getFirstItem();

    TypeReference<List<OrderExtraVariant>> EXTRAS_TYPE_REFERENCE = new TypeReference<>() {
    };

}
