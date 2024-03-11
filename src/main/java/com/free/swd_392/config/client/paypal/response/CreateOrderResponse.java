package com.free.swd_392.config.client.paypal.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.free.swd_392.config.client.paypal.common.Link;
import com.free.swd_392.config.client.paypal.common.OrderStatus;
import com.free.swd_392.config.client.paypal.common.PurchaseUnit;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateOrderResponse {

    @JsonProperty("id")
    private String id;
    @JsonProperty("status")
    private OrderStatus status;
    @JsonProperty("create_time")
    private LocalDateTime createTime;
    @JsonProperty("links")
    private List<Link> links;
    @JsonProperty("purchase_units")
    private List<PurchaseUnit> purchaseUnits;

    @JsonIgnore
    public String getApproveLink() {
        if (CollectionUtils.isEmpty(links)) {
            return null;
        }
        return this.links.stream()
                .filter(l -> "approve".equals(l.getRel()))
                .map(Link::getHref)
                .findFirst()
                .orElse(null);
    }
}
