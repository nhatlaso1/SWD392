package com.free.swd_392.core.factory;

import com.free.swd_392.core.model.BasePagingResponse;
import com.free.swd_392.core.model.BaseResponse;
import com.free.swd_392.exception.InvalidException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

public interface Factory<A, E> {

    JpaRepository<E, A> getRepository();

    default <T> ResponseEntity<BaseResponse<T>> success() {
        return ResponseEntity.ok(BaseResponse.<T>builder()
                .success(true)
                .build()
        );
    }

    default <D> ResponseEntity<BaseResponse<D>> success(D data) {
        return ResponseEntity.ok(BaseResponse.<D>builder()
                .success(true)
                .data(data)
                .build()
        );
    }

    default <D> ResponseEntity<BasePagingResponse<D>> success(BasePagingResponse<D> data) {
        return ResponseEntity.ok(data);
    }

    default E findById(A id, String errorMessage) throws InvalidException {
        return getRepository().findById(id)
                .orElseThrow(() -> new InvalidException(errorMessage));
    }
}
