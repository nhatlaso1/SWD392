package com.free.swd_392.repository.user;

import com.free.swd_392.entity.product.ProductCategoryEntity;
import com.free.swd_392.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserRepository extends
        JpaRepository<UserEntity, String>,
        JpaSpecificationExecutor<ProductCategoryEntity> {

}
