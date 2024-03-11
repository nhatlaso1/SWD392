package com.free.swd_392.config.startup;

import com.free.swd_392.entity.user.RoleEntity;
import com.free.swd_392.enums.RoleKind;
import com.free.swd_392.repository.user.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ImportRoleCommand implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() != 0) {
            return;
        }
        roleRepository.saveAll(
                List.of(
                        new RoleEntity().setKind(RoleKind.CMS),
                        new RoleEntity().setKind(RoleKind.SUPER_ADMIN),
                        new RoleEntity().setKind(RoleKind.USER),
                        new RoleEntity().setKind(RoleKind.MERCHANT)
                )
        );
    }
}
