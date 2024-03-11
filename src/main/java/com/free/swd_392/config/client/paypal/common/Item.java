package com.free.swd_392.config.client.paypal.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {

    @JsonProperty("name")
    private String name;
    @JsonProperty("quantity")
    private Integer quantity;
    @JsonProperty("description")
    private String description;
    @JsonProperty("unit_amount")
    private Money unitAmount;
}

