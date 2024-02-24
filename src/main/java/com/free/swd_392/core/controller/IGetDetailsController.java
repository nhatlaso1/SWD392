package com.free.swd_392.core.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.free.swd_392.core.converter.DetailsConverter;
import com.free.swd_392.core.converter.IdConverter;
import com.free.swd_392.core.factory.Factory;
import com.free.swd_392.core.factory.FactoryExceptionCode;
import com.free.swd_392.core.model.BaseResponse;
import com.free.swd_392.core.model.IBaseData;
import com.free.swd_392.core.view.View;
import com.free.swd_392.exception.InvalidException;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
public interface IGetDetailsController<I, D extends IBaseData<I>, A, E> extends
        Factory<A, E>,
        FactoryExceptionCode,
        IdConverter<I, A>,
        DetailsConverter<I, D, E> {

    @GetMapping("{id}/details")
    @JsonView(View.Details.class)
    default ResponseEntity<BaseResponse<D>> getDetailsById(@PathVariable("id") @NotNull I id) {
        preGetDetails(id);
        return success(aroundGetDetails(id));
    }

    default void preGetDetails(I id) {
        // empty
    }

    default D aroundGetDetails(I id) throws InvalidException {
        E entity = getRepository().findById(toIdEntity(id))
                .orElseThrow(() -> new InvalidException(notFound()));
        D details = convertToDetails(entity);
        postGetDetails(entity, details);
        return details;
    }

    default void postGetDetails(E entity, D details) throws InvalidException {
        // empty
    }
}
