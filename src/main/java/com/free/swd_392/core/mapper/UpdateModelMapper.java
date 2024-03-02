package com.free.swd_392.core.mapper;

import com.free.swd_392.core.model.IBaseData;
import org.hibernate.collection.spi.PersistentBag;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

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
        if (entities == null) {
            entities = new PersistentBag<>();
        }
        var oldDetailsMap = new HashMap<A, D>();
        var newDetailsList = new LinkedList<D>();
        for (var detail : details) {
            var detailId = getDetailsId.apply(detail);
            if (detailId != null) {
                oldDetailsMap.put(detailId, detail);
            } else {
                newDetailsList.add(detail);
            }
        }
        var entityIterator = entities.iterator();
        while (entityIterator.hasNext()) {
            var entity = entityIterator.next();
            var entityId = getEntityId.apply(entity);
            var detail = oldDetailsMap.get(entityId);
            if (detail == null) {
                entityIterator.remove();
                continue;
            }
            updateConvertToEntity(entity, oldDetailsMap.get(entityId));
        }
        for (var detail : newDetailsList) {
            var entity = entityConstructor.get();
            updateConvertToEntity(entity, detail);
            entities.add(entity);
        }
    }
}
