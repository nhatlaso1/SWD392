package com.free.swd_392.core.mapper;

import com.free.swd_392.core.model.IBaseData;
import org.mapstruct.Named;

public interface CreateModelMapper<D extends IBaseData<?>, E> {

    @Named("createConvertToEntityMapper")
    E createConvertToEntity(D request);
}
