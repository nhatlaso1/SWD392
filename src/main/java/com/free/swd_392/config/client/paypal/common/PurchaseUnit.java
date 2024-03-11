package com.free.swd_392.config.client.paypal.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseUnit {

    @JsonProperty("reference_id")
    private String referenceId;
    @JsonProperty("description")
    private String description;
    @JsonProperty("custom_id")
    private String customId;
    @JsonProperty("amount")
    private Amount amount;
    @Singular
    @JsonProperty("items")
    private List<Item> items;
    @JsonProperty("shipping")
    private Shipping shipping;
}
