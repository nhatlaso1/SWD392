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
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
public interface ICreateModelController<I, D extends IBaseData<I>, A, E> extends
        Factory<A, E>,
        FactoryExceptionCode,
        IdConverter<I, A>,
        DetailsConverter<I, D, E> {

    @PostMapping("create")
    @JsonView({View.Details.class})
    @Transactional
    default ResponseEntity<BaseResponse<D>> createModel(
            @JsonView(View.Include.Create.class)
            @RequestBody
            @Valid
            D request
    ) {
        return success(aroundCreate(preCreate(request)));
    }

    default D preCreate(D details) {
        if (details.getId() == null) {
            return details;
        }
        A entityId = toIdEntity(details.getId());
        if (getRepository().findById(entityId).isPresent()) {
            throw new InvalidException(conflict());
        }
        return details;
    }

    default D aroundCreate(D details) {
        E entity = createConvertToEntity(details);
        entity = getRepository().save(entity);
        postCreate(entity, details);
        return convertToDetails(entity);
    }

    default void postCreate(E entity, D details) {
        // empty
    }

    E createConvertToEntity(D details);
}
