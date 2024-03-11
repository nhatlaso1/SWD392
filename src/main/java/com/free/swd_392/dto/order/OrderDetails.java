package com.free.swd_392.dto.order;

import lombok.Data;

import java.util.List;

@Data
public class OrderDetails extends OrderInfo {

    private String paymentUrl;
    private List<OrderItemInfo> orderItems;
}
