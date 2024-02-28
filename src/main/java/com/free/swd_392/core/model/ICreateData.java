package com.free.swd_392.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface ICreateData<I> extends IBaseData<I> {

    @Override
    @JsonIgnore
    default I getId() {
        return null;
    }
}
