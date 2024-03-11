package com.free.swd_392.config.client.paypal.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.free.swd_392.config.client.currency.CurrencyCode;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Amount extends Money {

    @JsonProperty("breakdown")
    private Breakdown breakdown;

    @Builder
    public Amount(Double value, CurrencyCode currencyCode, Breakdown breakdown) {
        super(value, currencyCode);
        this.breakdown = breakdown;
    }

    @Builder
    public Amount(Double value, Breakdown breakdown) {
        super(value);
        this.setCurrencyCode(CurrencyCode.getDefault());
        this.breakdown = breakdown;
    }

    @Getter
    @Setter
    @Builder
    public static class Breakdown {

        @JsonProperty("item_total")
        private Money itemTotal;
        @JsonProperty("shipping")
        private Money shipping;
        @JsonProperty("shipping_discount")
        private Money shippingDiscount;
        @JsonProperty("discount")
        private Money discount;

        /**
         * item_total + tax_total + shipping + handling + insurance - shipping_discount - discount
         *
         * @return total
         */
        public Double getTotal() {
            Double total = 0.0;
            if (itemTotal != null) {
                total += itemTotal.getValue();
            }
            if (shipping != null) {
                total += shipping.getValue();
            }
            if (shippingDiscount != null) {
                total += shippingDiscount.getValue();
            }
            if (discount != null) {
                total += discount.getValue();
            }
            return total;
        }
    }

    public static class AmountBuilder {

        private AmountBuilder value(final Double value) {
            return this;
        }

        public Amount build() {
            this.value = breakdown.getTotal();
            return new Amount(this.value, this.currencyCode, this.breakdown);
        }
    }
}
