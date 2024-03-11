package com.free.swd_392.config.client.paypal.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.free.swd_392.config.client.currency.CurrencyCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Money {

    @JsonProperty("value")
    private Double value;
    @JsonProperty(value = "currency_code")
    private CurrencyCode currencyCode = CurrencyCode.getDefault();

    public Money(Double value) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        this.value = value;
    }

    public Double getValue() {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        BigDecimal number = BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_EVEN);
        return number.doubleValue();
    }
}
