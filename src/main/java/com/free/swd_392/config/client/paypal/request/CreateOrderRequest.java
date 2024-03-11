package com.free.swd_392.config.client.paypal.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.free.swd_392.config.client.paypal.common.OrderIntent;
import com.free.swd_392.config.client.paypal.common.PayPalAppContext;
import com.free.swd_392.config.client.paypal.common.PurchaseUnit;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Builder
@Jacksonized
public class CreateOrderRequest {

    @JsonProperty("intent")
    private OrderIntent intent;

    @JsonProperty("purchase_units")
    @Singular
    private List<PurchaseUnit> purchaseUnits;

    @JsonProperty("application_context")
    private PayPalAppContext applicationContext;
}
