package com.free.swd_392.repository.payment;

import com.free.swd_392.entity.payment.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface PaymentRepository extends
        JpaRepository<PaymentEntity, UUID>,
        JpaSpecificationExecutor<PaymentEntity> {

}
