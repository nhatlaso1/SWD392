package com.free.swd_392.config.client.paypal.common;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class Link {

    private String href;
    private String rel;
    private String method;
}
