package com.free.swd_392.controller;

import com.free.swd_392.core.model.BaseResponse;
import org.springframework.http.ResponseEntity;

import java.util.function.Supplier;

public abstract class BaseController {

    protected <T> ResponseEntity<BaseResponse<T>> wrapResponse(T data) {
        return ResponseEntity.ok(
                BaseResponse.<T>builder()
                        .data(data)
                        .success(true)
                        .build()
        );
    }

    protected <T> ResponseEntity<BaseResponse<T>> wrapResponse(Supplier<T> supplier) {
        return ResponseEntity.ok(
                BaseResponse.<T>builder()
                        .data(supplier.get())
                        .success(true)
                        .build()
        );
    }
}
