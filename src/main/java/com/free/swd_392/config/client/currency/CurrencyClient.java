package com.free.swd_392.config.client.currency;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "currency"
)
public interface CurrencyClient {

    @GetMapping(value = "/v6/latest/{baseCode}")
    Currency getCurrencyRates(@PathVariable("baseCode") CurrencyCode baseCode);
}
