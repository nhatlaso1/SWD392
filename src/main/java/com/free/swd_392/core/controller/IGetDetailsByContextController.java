package com.free.swd_392.core.controller;

import com.free.swd_392.core.converter.DetailsConverter;
import com.free.swd_392.core.factory.Factory;
import com.free.swd_392.core.factory.FactoryExceptionCode;
import com.free.swd_392.core.model.BaseResponse;
import com.free.swd_392.core.model.IBaseData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
public interface IGetDetailsByContextController<I, D extends IBaseData<I>, A, E> extends
        Factory<A, E>,
        FactoryExceptionCode,
        DetailsConverter<I, D, E> {

    @GetMapping("details/context")
    ResponseEntity<BaseResponse<D>> getDetailsByContext();
}
