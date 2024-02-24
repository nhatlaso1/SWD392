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
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
public interface IUpdateModelController<I, D extends IBaseData<I>, A, E> extends
        Factory<A, E>,
        FactoryExceptionCode,
        IdConverter<I, A>,
        DetailsConverter<I, D, E> {

    @PutMapping("update")
    @JsonView(View.Details.class)
    default ResponseEntity<BaseResponse<D>> updateModel(
            @JsonView(View.Include.Update.class)
            @RequestBody
            @Valid
            D request
    ) {
        if (request.getId() == null) {
            throw new InvalidException(badRequest());
        }
        return success(aroundUpdate(preUpdate(request)));
    }

    default D preUpdate(D details) {
        return details;
    }

    default D aroundUpdate(D details) {
        CrudRepository<E, A> repository = getRepository();
        E oldEntity = repository.findById(toIdEntity(details.getId()))
                .orElseThrow(() -> new InvalidException(notFound()));
        updateConvertToEntity(oldEntity, details);
        E entity = repository.save(oldEntity);
        postUpdate(entity, details);
        return convertToDetails(entity);
    }

    default void postUpdate(E entity, D details) {
        // empty
    }

    void updateConvertToEntity(E entity, D details) throws InvalidException;
}
