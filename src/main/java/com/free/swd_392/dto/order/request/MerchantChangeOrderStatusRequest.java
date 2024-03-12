package com.free.swd_392.dto.order.request;

import com.free.swd_392.enums.OrderStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class MerchantChangeOrderStatusRequest {

    private UUID id;
    private OrderStatus status;
}
