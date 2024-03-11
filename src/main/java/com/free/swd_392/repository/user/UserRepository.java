package com.free.swd_392.repository.user;

import com.free.swd_392.entity.product.ProductCategoryEntity;
import com.free.swd_392.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserRepository extends
        JpaRepository<UserEntity, String>,
        JpaSpecificationExecutor<ProductCategoryEntity> {

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    @Modifying
    @Query(value = """
                UPDATE UserEntity u
                SET u.active = :active
                WHERE u.id = :userId
            """)
    void updateStatus(@Param("userId") String userId, @Param("active") Boolean active);

}
