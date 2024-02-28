package com.free.swd_392.core.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.free.swd_392.core.converter.IdConverter;
import com.free.swd_392.core.converter.ListEntityToListInfoConverter;
import com.free.swd_392.core.factory.Factory;
import com.free.swd_392.core.factory.FactoryExceptionCode;
import com.free.swd_392.core.model.BaseResponse;
import com.free.swd_392.core.model.IBaseData;
import com.free.swd_392.core.view.View;
import com.free.swd_392.exception.InvalidException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/")
public interface IGetInfoListNonFilterController<I, T extends IBaseData<I>, A, E> extends
        Factory<A, E>,
        FactoryExceptionCode,
        IdConverter<I, A>,
        ListEntityToListInfoConverter<I, T, E> {

    @GetMapping("list")
    @JsonView(View.Info.class)
    @Transactional
    default ResponseEntity<BaseResponse<List<T>>> getInfoList() {
        preGetInfoListNonFilter();
        return success(aroundGetInfoListNonFilter());
    }

    default void preGetInfoListNonFilter() {
        // empty
    }

    default List<T> aroundGetInfoListNonFilter() {
        try {
            return convertToInfoList(getRepository().findAll());
        } catch (Exception e) {
            throw new InvalidException(internalServerError());
        }
    }
}
