package com.free.swd_392.repository.user;

import com.free.swd_392.entity.user.UserAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserAddressRepository extends
        JpaRepository<UserAddressEntity, Long>,
        JpaSpecificationExecutor<UserAddressEntity> {

    @Modifying
    @Query(value = """
                UPDATE auction_swd_392.auction_user_address uae
                SET is_default = CASE
                                    WHEN id = :addressId THEN true
                                    ELSE false
                                END
                WHERE user_id = :userId
            """, nativeQuery = true)
    void updateUserAddressDefault(@Param("userId") Long userId, @Param("addressId") Long addressId);
}
