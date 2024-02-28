package com.free.swd_392.core.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.free.swd_392.core.converter.IdConverter;
import com.free.swd_392.core.converter.InfoConverter;
import com.free.swd_392.core.factory.Factory;
import com.free.swd_392.core.factory.FactoryExceptionCode;
import com.free.swd_392.core.model.BaseResponse;
import com.free.swd_392.core.model.IBaseData;
import com.free.swd_392.core.view.View;
import com.free.swd_392.exception.InvalidException;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
public interface IGetInfoController<I, T extends IBaseData<I>, A, E> extends
        Factory<A, E>,
        FactoryExceptionCode,
        IdConverter<I, A>,
        InfoConverter<I, T, E> {

    @GetMapping("{id}/info")
    @JsonView(View.Info.class)
    @Transactional
    default ResponseEntity<BaseResponse<T>> getInfoById(@PathVariable("id") @NotNull I id) {
        preGetInfo(id);
        return success(aroundGetInfo(id));
    }

    default void preGetInfo(I id) {
        // empty
    }

    default T aroundGetInfo(I id) {
        E entity = getRepository().findById(toIdEntity(id))
                .orElseThrow(() -> new InvalidException(notFound()));
        T info = convertToInfo(entity);
        postInfo(entity, info);
        return info;
    }

    default void postInfo(E entity, T info) throws InvalidException {
        // empty
    }
}
