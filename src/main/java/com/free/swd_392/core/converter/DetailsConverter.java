package com.free.swd_392.core.converter;

import com.free.swd_392.core.model.IBaseData;

public interface DetailsConverter<I, D extends IBaseData<I>, E> {

    D convertToDetails(E entity);
}
