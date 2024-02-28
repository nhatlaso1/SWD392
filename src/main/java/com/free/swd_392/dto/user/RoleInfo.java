package com.free.swd_392.dto.user;

import com.free.swd_392.dto.AuditInfo;
import com.free.swd_392.enums.RoleKind;
import lombok.Data;

import java.util.UUID;

@Data
public class RoleInfo extends AuditInfo<UUID> {

    private UUID id;
    private RoleKind kind;
}
