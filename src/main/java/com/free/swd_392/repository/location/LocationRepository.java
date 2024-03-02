package com.free.swd_392.repository.location;

import com.free.swd_392.entity.location.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface LocationRepository extends
        JpaRepository<LocationEntity, Long>,
        JpaSpecificationExecutor<LocationEntity> {

}
