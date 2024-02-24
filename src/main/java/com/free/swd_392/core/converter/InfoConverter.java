package com.free.swd_392.core.converter;

import com.free.swd_392.core.model.IBaseData;

public interface InfoConverter<I , T extends IBaseData<I>, E> {

    T convertToInfo(E entity);
}
