package com.free.swd_392.core.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.free.swd_392.core.converter.DetailsConverter;
import com.free.swd_392.core.converter.IdConverter;
import com.free.swd_392.core.factory.Factory;
import com.free.swd_392.core.factory.FactoryExceptionCode;
import com.free.swd_392.core.mapper.UpdateModelMapper;
import com.free.swd_392.core.model.BaseResponse;
import com.free.swd_392.core.model.IBaseData;
import com.free.swd_392.core.view.View;
import com.free.swd_392.exception.InvalidException;
import jakarta.validation.Valid;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
public interface IUpdateModelController<I, D extends IBaseData<I>, U extends IBaseData<I>, A, E> extends
        Factory<A, E>,
        FactoryExceptionCode,
        IdConverter<I, A>,
        DetailsConverter<I, D, E>,
        UpdateModelMapper<U, E> {

    @PutMapping("update")
    @JsonView(View.Details.class)
    @Transactional
    default ResponseEntity<BaseResponse<D>> updateModel(
            @JsonView(View.Include.Update.class)
            @RequestBody
            @Valid
            U request
    ) {
        return success(aroundUpdate(preUpdate(request)));
    }

    default U preUpdate(U request) {
        if (request.getId() == null) {
            throw new InvalidException(badRequest());
        }
        return request;
    }

    @Nullable
    default D aroundUpdate(U request) {
        CrudRepository<E, A> repository = getRepository();
        E oldEntity = repository.findById(toIdEntity(request.getId()))
                .orElseThrow(() -> new InvalidException(notFound()));
        updateConvertToEntity(oldEntity, request);
        E entity = repository.save(oldEntity);
        D details = null;
        if (returnResultAfterUpdate()) {
            details = convertToDetails(entity);
        }
        postUpdate(entity, request, details);
        return null;
    }

    default void postUpdate(E entity, U request, @Nullable D details) {
        // empty
    }

    default boolean returnResultAfterUpdate() {
        return false;
    }
}
