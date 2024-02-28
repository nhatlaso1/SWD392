package com.free.swd_392.core.mapper;

import com.free.swd_392.core.model.IBaseData;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public interface UpdateModelMapper<D extends IBaseData<?>, E> {

    @Named("updateConvertToEntityMapper")
    void updateConvertToEntity(@MappingTarget E entity, D details);

    @Named("updateConvertToEntityListMapper")
    default <A> void updateConvertToEntityList(@MappingTarget List<E> entities,
                                               List<D> details,
                                               Function<E, A> getEntityId,
                                               Function<D, A> getDetailsId,
                                               Supplier<E> entityConstructor) {
        if (CollectionUtils.isEmpty(details)) {
            return;
        }
        if (CollectionUtils.isEmpty(entities)) {
            entities = new LinkedList<>();
        }
        var entitiesMap = entities.stream()
                .collect(Collectors.toMap(
                        getEntityId,
                        Function.identity()
                ));
        entities.clear();
        for (var detail : details) {
            var detailId = getDetailsId.apply(detail);
            var entity = entitiesMap.get(detailId);
            if (entity == null) {
                entity = entityConstructor.get();
            }
            updateConvertToEntity(entity, detail);
            entities.add(entity);
        }
    }
}
