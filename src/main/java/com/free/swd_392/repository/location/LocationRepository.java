package com.free.swd_392.repository.location;

import com.free.swd_392.entity.location.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface LocationRepository extends
        JpaRepository<LocationEntity, Long>,
        JpaSpecificationExecutor<LocationEntity> {

    @Query(value = """
                SELECT CASE WHEN COUNT(1) > 0 THEN true ELSE false END
                FROM LocationEntity p
                    INNER JOIN LocationEntity d ON p.id = d.parentId
                    INNER JOIN LocationEntity w ON d.id = w.parentId
                WHERE p.id = :provinceId
                    AND d.id = :districtId
                    AND w.id = :wardId
            """)
    boolean existsLocationPath(@Param("provinceId") Long provinceId, @Param("districtId") Long districtId, @Param("wardId") Long wardId);
}
