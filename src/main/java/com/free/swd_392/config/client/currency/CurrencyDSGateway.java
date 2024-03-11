package com.free.swd_392.config.client.currency;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.index.qual.NonNegative;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class CurrencyDSGateway implements InitializingBean {

    private final CurrencyClient currencyClient;
    private Cache<CurrencyCode, Currency> currencyCache;

    public Currency getByCode(CurrencyCode currencyCode) {
        var currency = currencyCache.getIfPresent(currencyCode);
        if (currency == null) {
            currency = currencyClient.getCurrencyRates(currencyCode);
            currencyCache.put(currencyCode, currency);
        }
        return currency;
    }

    public Double getRates(CurrencyCode source, CurrencyCode target) {
        var currency = getByCode(source);
        return currency.getRate(target);
    }

    @Override
    public void afterPropertiesSet() {
        Expiry<CurrencyCode, Currency> expiry = new Expiry<>() {
            @Override
            public long expireAfterCreate(CurrencyCode s, Currency currency, long l) {
                return Duration.ofSeconds(currency.getTtl()).toNanos();
            }

            @Override
            public long expireAfterUpdate(CurrencyCode s, Currency currency, long l, @NonNegative long l1) {
                return l1;
            }

            @Override
            public long expireAfterRead(CurrencyCode s, Currency currency, long l, @NonNegative long l1) {
                return l1;
            }
        };
        currencyCache = Caffeine.newBuilder()
                .expireAfter(expiry)
                .build();
    }
}
