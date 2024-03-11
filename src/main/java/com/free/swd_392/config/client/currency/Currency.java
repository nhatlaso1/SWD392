package com.free.swd_392.config.client.currency;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.free.swd_392.config.client.paypal.common.Money;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Currency {

    @Id
    @JsonProperty("base_code")
    private CurrencyCode baseCode;
    @JsonProperty("rates")
    private Map<String, Double> rates;
    @JsonProperty("time_next_update_unix")
    private long timeNextUpdateUnix = 0;

    @JsonIgnore
    public long getTtl() {
        return timeNextUpdateUnix + 60L - Duration.ofMillis(System.currentTimeMillis()).toSeconds();
    }

    public double getRate(CurrencyCode code) {
        Double rate = this.rates.get(code.name());
        if (rate == null) {
            throw new IllegalArgumentException();
        }
        return rate;
    }

    public Money convert(Money money, CurrencyCode toCode) {
        double fromRate = getRate(money.getCurrencyCode());
        double toRate = getRate(toCode);
        double moneyValue = (toRate * money.getValue()) / fromRate;
        return new Money(moneyValue, toCode);
    }

    public Money convert(BigDecimal sourceValue, CurrencyCode source, CurrencyCode target) {
        BigDecimal fromRate = BigDecimal.valueOf(getRate(source));
        BigDecimal toRate = BigDecimal.valueOf(getRate(target));
        BigDecimal convertedValue = toRate.multiply(sourceValue).divide(fromRate, 2, RoundingMode.HALF_EVEN);
        return new Money(convertedValue.doubleValue(), target);
    }
}
