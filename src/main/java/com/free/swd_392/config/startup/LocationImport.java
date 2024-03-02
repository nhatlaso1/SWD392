package com.free.swd_392.config.startup;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.free.swd_392.entity.location.LocationEntity;
import com.free.swd_392.enums.LocationKind;
import com.free.swd_392.repository.location.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;


@Component
@RequiredArgsConstructor
public class LocationImport implements CommandLineRunner {

    private final LocationRepository locationRepository;
    private final ObjectMapper objectMapper;
    @Value("classpath:data/location.json")
    private final Resource locationResource;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (locationRepository.count() > 0) {
            return;
        }

        var locationArray = objectMapper.reader().readTree(locationResource.getInputStream());
        var provinceList = new ArrayList<LocationEntity>();
        for (var provinceNode : locationArray) {
            var province = new LocationEntity(getName(provinceNode), LocationKind.PROVINCE);
            for (var districtNode : provinceNode.get("districts")) {
                var district = new LocationEntity(getName(districtNode), LocationKind.DISTRICT, province);
                province.addChild(district);
                for (var wardNode : districtNode.get("wards")) {
                    var ward = new LocationEntity(getName(wardNode), LocationKind.WARD, district);
                    district.addChild(ward);
                }
            }
            provinceList.add(province);
        }
        locationRepository.saveAll(provinceList);
    }

    private String getName(JsonNode node) {
        return node.get("name").asText();
    }
}
