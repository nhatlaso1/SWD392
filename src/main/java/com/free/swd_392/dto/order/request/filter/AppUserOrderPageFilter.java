package com.free.swd_392.dto.order.request.filter;

import com.free.swd_392.core.model.IPageFilter;
import com.free.swd_392.enums.OrderStatus;
import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

@Data
public class AppUserOrderPageFilter implements IPageFilter {

    private Pageable pageable;
    private OrderStatus status;
    private UUID id;
    private String productName;
    private String shopName;
    private String userId;
}
