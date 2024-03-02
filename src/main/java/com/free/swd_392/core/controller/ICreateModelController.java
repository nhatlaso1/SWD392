package com.free.swd_392.core.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.free.swd_392.core.converter.DetailsConverter;
import com.free.swd_392.core.converter.IdConverter;
import com.free.swd_392.core.factory.Factory;
import com.free.swd_392.core.factory.FactoryExceptionCode;
import com.free.swd_392.core.mapper.CreateModelMapper;
import com.free.swd_392.core.model.BaseResponse;
import com.free.swd_392.core.model.IBaseData;
import com.free.swd_392.core.view.View;
import com.free.swd_392.exception.InvalidException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
public interface ICreateModelController<I, D extends IBaseData<I>, C extends IBaseData<I>, A, E> extends
        Factory<A, E>,
        FactoryExceptionCode,
        IdConverter<I, A>,
        DetailsConverter<I, D, E>,
        CreateModelMapper<C, E> {

    @PostMapping("create")
    @JsonView({View.Details.class})
    @Transactional
    default ResponseEntity<BaseResponse<D>> createModel(
            @JsonView(View.Include.Create.class)
            @RequestBody
            @Valid
            C request
    ) {
        return success(aroundCreate(preCreate(request)));
    }

    default C preCreate(C request) {
        if (request.getId() == null) {
            return request;
        }
        A entityId = toIdEntity(request.getId());
        if (getRepository().findById(entityId).isPresent()) {
            throw new InvalidException(conflict());
        }
        return request;
    }

    @Nullable
    default D aroundCreate(C request) {
        E entity = createConvertToEntity(request);
        entity = getRepository().save(entity);
        D details = null;
        if (returnResultAfterCreate()) {
            details = convertToDetails(entity);
        }
        postCreate(entity, request, details);
        return null;
    }

    default void postCreate(E entity, C request, @Nullable D details) {
        // empty
    }

    default boolean returnResultAfterCreate() {
        return false;
    }
}
