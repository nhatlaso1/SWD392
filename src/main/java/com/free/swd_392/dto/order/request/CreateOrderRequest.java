package com.free.swd_392.dto.order.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.free.swd_392.core.model.ICreateData;
import com.free.swd_392.enums.order.PaymentMethodProvider;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class CreateOrderRequest implements ICreateData<UUID> {

    @NotNull
    private Long userAddressId;
    @Size(max = 16777215)
    private String description;
    @NotNull
    private PaymentMethodProvider paymentMethod;
    @Size(min = 1)
    private List<@Valid CreateOrderItemRequest> orderItems;

    @JsonIgnore
    @Setter(value = AccessLevel.NONE)
    private Map<UUID, CreateOrderItemRequest> orderItemMap;

    public Map<UUID, CreateOrderItemRequest> getOrderItemMap() {
        if (orderItemMap != null) {
            return orderItemMap;
        }
        orderItemMap = orderItems.stream()
                .collect(Collectors.toMap(
                        CreateOrderItemRequest::getSkuId,
                        Function.identity()
                ));
        return orderItemMap;
    }
}
