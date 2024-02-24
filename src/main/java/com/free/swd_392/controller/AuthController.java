package com.free.swd_392.controller;

import com.free.swd_392.core.model.BaseResponse;
import com.free.swd_392.dto.user.UserDetails;
import com.free.swd_392.dto.user.request.RegisterUserRequest;
import com.free.swd_392.enums.RoleKind;
import com.free.swd_392.exception.InvalidException;
import com.free.swd_392.mapper.UserMapper;
import com.free.swd_392.repository.user.RoleRepository;
import com.free.swd_392.repository.user.UserRepository;
import com.free.swd_392.shared.model.event.UserCreatedEvent;
import com.free.swd_392.shared.utils.JwtUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@Tag(name = "Auth Controller")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController extends BaseController {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final FirebaseAuth firebaseAuth;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<BaseResponse<UserDetails>> register(@RequestBody RegisterUserRequest request) {
        String userId = JwtUtils.getUserId();
        try {
            if (userRepository.existsById(userId)) {
                throw new InvalidException("Subject id existed");
            }
            var userRecord = firebaseAuth.getUser(JwtUtils.getUserId());
            var user = userMapper.fromUserRecordToEntity(userRecord);
            userMapper.updateInfoRegisterToUserEntity(user, request);

            var role = roleRepository.findFirstByKind(RoleKind.USER)
                    .orElseThrow(() -> new InvalidException("Role not found"));
            user.setRoleId(role.getId());
            firebaseAuth.setCustomUserClaims(
                    userRecord.getUid(),
                    Map.of("resource_access", Map.of(
                                    "auction", Map.of(
                                            "roles",
                                            List.of("ROLE_" + role.getKind().name())
                                    )
                            )
                    )
            );
            user = userRepository.save(user);
            applicationEventPublisher.publishEvent(new UserCreatedEvent(user.getId()));
            return wrapResponse(userMapper.convertToDetail(user));
        } catch (FirebaseAuthException e) {
            log.warn("Get user record from Firebase error ", e);
            throw new InvalidException("Get user record from Firebase error", e);
        }
    }
}
