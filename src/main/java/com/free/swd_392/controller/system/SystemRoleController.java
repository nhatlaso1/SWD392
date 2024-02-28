package com.free.swd_392.controller.system;

import com.free.swd_392.core.controller.IGetInfoListNonFilterController;
import com.free.swd_392.dto.user.RoleInfo;
import com.free.swd_392.entity.user.RoleEntity;
import com.free.swd_392.mapper.RoleMapper;
import com.free.swd_392.repository.user.RoleRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@Tag(name = "System Role Controller")
@RestController
@RequestMapping("/api/v1/system/role")
@RequiredArgsConstructor
public class SystemRoleController implements
        IGetInfoListNonFilterController<UUID, RoleInfo, UUID, RoleEntity> {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public RoleInfo convertToInfo(RoleEntity entity) {
        return roleMapper.convertToInfo(entity);
    }

    @Override
    public JpaRepository<RoleEntity, UUID> getRepository() {
        return roleRepository;
    }
}
