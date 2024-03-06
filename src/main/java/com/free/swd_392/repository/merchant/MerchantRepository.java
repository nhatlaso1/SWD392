package com.free.swd_392.repository.merchant;

import com.free.swd_392.entity.merchant.MerchantEntity;
import com.free.swd_392.enums.MerchantStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface MerchantRepository extends
        JpaRepository<MerchantEntity, Long>,
        JpaSpecificationExecutor<MerchantEntity> {

    Optional<MerchantEntity> findByOwnerId(String ownerId);

    Optional<MerchantEntity> findByIdAndStatus(Long id, MerchantStatus status);
}
