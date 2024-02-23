package com.free.swd_392.controller;

import com.free.swd_392.dto.common.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.function.Supplier;

public abstract class BaseController {

    protected <T> ResponseEntity<BaseResponse<T>> wrapResponse(T data) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        BaseResponse.<T>builder()
                                .data(data)
                                .success(true)
                                .build()
                );
    }

    protected <T> ResponseEntity<BaseResponse<T>> wrapResponse(Supplier<T> supplier) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        BaseResponse.<T>builder()
                                .data(supplier.get())
                                .success(true)
                                .build()
                );
    }
}
