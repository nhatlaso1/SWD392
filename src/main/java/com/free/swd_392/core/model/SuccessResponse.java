package com.free.swd_392.core.model;

import lombok.Data;

@Data
public class SuccessResponse extends BaseResponse<Object> {

    public static final SuccessResponse SUCCESS = new SuccessResponse(true);
    public static final SuccessResponse FAILED = new SuccessResponse(false);

    private SuccessResponse(boolean success) {
        setSuccess(success);
    }
}
