package com.free.swd_392.controller;

import com.free.swd_392.core.controller.IGetInfoListWithFilterController;
import com.free.swd_392.dto.location.LocationInfo;
import com.free.swd_392.dto.location.request.LocationFilter;
import com.free.swd_392.entity.location.LocationEntity;
import com.free.swd_392.mapper.LocationMapper;
import com.free.swd_392.repository.location.LocationRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Location Controller")
@Transactional
@RestController
@RequestMapping("/api/v1/location")
@RequiredArgsConstructor
public class LocationController extends BaseController implements
        IGetInfoListWithFilterController<Long, LocationInfo, Long, LocationEntity, LocationFilter> {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Override
    public LocationInfo convertToInfo(LocationEntity entity) {
        return locationMapper.convertToInfo(entity);
    }

    @Override
    public JpaRepository<LocationEntity, Long> getRepository() {
        return locationRepository;
    }
}
