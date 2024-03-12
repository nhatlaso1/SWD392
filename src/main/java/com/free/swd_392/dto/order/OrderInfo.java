package com.free.swd_392.dto.order;

import com.free.swd_392.core.model.IBaseData;
import com.free.swd_392.enums.OrderStatus;
import com.free.swd_392.enums.order.PaymentMethodProvider;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class OrderInfo implements IBaseData<UUID> {

    private UUID id;
    private LocalDateTime createdDate;
    private String receiverFullName;
    private String phone;
    private Long provinceId;
    private String provinceName;
    private Long districtId;
    private String districtName;
    private Long wardId;
    private String wardName;
    private String addressDetails;
    private BigDecimal subTotal = BigDecimal.ZERO;
    private BigDecimal shippingCharge = BigDecimal.ZERO;
    private String description;
    private PaymentMethodProvider paymentMethod;
    private OrderStatus status;
    private Integer numItem;
    private OrderItemInfo firstItem;
}
