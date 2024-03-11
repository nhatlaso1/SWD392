package com.free.swd_392.repository.order;

import com.free.swd_392.entity.order.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface OrderItemRepository extends
        JpaRepository<OrderItemEntity, Long>,
        JpaSpecificationExecutor<OrderItemEntity> {

    Optional<OrderItemEntity> findFirstByOrderId(UUID orderId);
}
