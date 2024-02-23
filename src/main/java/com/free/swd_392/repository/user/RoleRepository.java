package com.free.swd_392.repository.user;

import com.free.swd_392.entity.user.RoleEntity;
import com.free.swd_392.enums.RoleKind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {

    Optional<RoleEntity> findFirstByKind(RoleKind kind);
}
