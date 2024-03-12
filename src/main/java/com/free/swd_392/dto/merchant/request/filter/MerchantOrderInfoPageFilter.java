package com.free.swd_392.dto.merchant.request.filter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.free.swd_392.core.model.IPageFilter;
import com.free.swd_392.enums.OrderStatus;
import com.free.swd_392.enums.order.PaymentMethodProvider;
import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

@Data
public class MerchantOrderInfoPageFilter implements IPageFilter {

    private Pageable pageable;
    private UUID id;
    private OrderStatus status;
    private String receiverFullName;
    private String phone;
    private PaymentMethodProvider paymentMethod;
    @JsonIgnore
    private Long merchantId;
}
