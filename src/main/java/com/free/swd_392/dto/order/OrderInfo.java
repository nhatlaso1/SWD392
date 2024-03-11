package com.free.swd_392.dto.order;

import com.free.swd_392.core.model.IBaseData;
import com.free.swd_392.enums.OrderStatus;
import com.free.swd_392.enums.order.PaymentMethodProvider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class OrderInfo implements IBaseData<UUID> {

    private UUID id;
    private LocalDateTime createdDate;
    @NotBlank
    @Size(max = 50)
    private String receiverFullName;
    @NotBlank
    @Size(max = 10)
    private String phone;
    @NotNull
    private Long provinceId;
    private String provinceName;
    @NotNull
    private Long districtId;
    private String districtName;
    @NotNull
    private Long wardId;
    private String wardName;
    @NotBlank
    @Size(max = 150)
    private String addressDetails;
    private BigDecimal subTotal = BigDecimal.ZERO;
    private BigDecimal shippingCharge = BigDecimal.ZERO;
    private String description;
    private PaymentMethodProvider paymentMethod;
    @NotNull
    private OrderStatus status;
    private Integer numItem;
    private OrderItemInfo firstItem;
}
