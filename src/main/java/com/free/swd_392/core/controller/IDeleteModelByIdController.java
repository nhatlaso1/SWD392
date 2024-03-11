package com.free.swd_392.core.controller;

import com.free.swd_392.core.converter.IdConverter;
import com.free.swd_392.core.factory.Factory;
import com.free.swd_392.core.factory.FactoryExceptionCode;
import com.free.swd_392.core.model.SuccessResponse;
import com.free.swd_392.exception.InvalidException;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Validated
@RequestMapping("/")
public interface IDeleteModelByIdController<I, A, E> extends
        Factory<A, E>,
        FactoryExceptionCode,
        IdConverter<I, A> {

    @DeleteMapping("{id}/delete")
    @Transactional
    default ResponseEntity<SuccessResponse> deleteModelById(@PathVariable("id") @NotNull I id) {
        preDelete(id);
        aroundDelete(id);
        postDelete(id);
        return success();
    }

    default void preDelete(I id) {
        // empty
    }

    default void aroundDelete(I id) {
        try {
            getRepository().deleteById(toIdEntity(id));
        } catch (Exception e) {
            throw new InvalidException(badRequest());
        }
    }

    default void postDelete(I id) {
        // empty
    }
}
