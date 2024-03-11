package com.free.swd_392.mapper.app;

import com.free.swd_392.core.mapper.DtoMapper;
import com.free.swd_392.dto.order.OrderItemInfo;
import com.free.swd_392.entity.order.OrderItemEntity;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public abstract class AppOrderItemMapper implements
        DtoMapper<OrderItemInfo, OrderItemInfo, OrderItemEntity> {
}
