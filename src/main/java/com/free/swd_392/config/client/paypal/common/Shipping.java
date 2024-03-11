package com.free.swd_392.config.client.paypal.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class Shipping {

    @JsonProperty("type")
    private ShippingType type;
    @JsonProperty("name")
    private Name name;
    @JsonProperty("address")
    private Address address;


    @Data
    @Builder
    @Jacksonized
    public static class Name {
        @JsonProperty("given_name")
        private String givenName;
        @JsonProperty("sur_name")
        private String surName;
    }

    @Data
    @Builder
    @Jacksonized
    public static class Address {
        @JsonProperty("address_line_1")
        private String addressLine1;
        @Builder.Default
        @JsonProperty("admin_area_2")
        private String adminArea2 = "VNM";
        @JsonProperty("country_code")
        @Builder.Default
        private String countryCode = "VN";
    }
}
