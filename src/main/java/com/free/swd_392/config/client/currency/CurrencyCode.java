package com.free.swd_392.config.client.currency;

public enum CurrencyCode {

    VND,
    USD;

    public static CurrencyCode getDefault() {
        return USD;
    }
}
