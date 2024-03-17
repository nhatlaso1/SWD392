package com.free.swd_392.shared.projection;

import com.free.swd_392.enums.OrderStatus;
import com.free.swd_392.enums.order.PaymentMethodProvider;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Projection for {@link com.free.swd_392.entity.order.OrderEntity}
 */
public interface MerchantOrderInfoProjection {
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
}
