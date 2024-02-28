package com.free.swd_392.core.mapper;

import org.mapstruct.Named;

public interface CreateModelMapper<D, E> {

    @Named("createConvertToEntityMapper")
    E createConvertToEntity(D details);
}
