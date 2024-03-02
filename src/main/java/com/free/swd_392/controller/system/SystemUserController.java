package com.free.swd_392.controller.system;

import com.free.swd_392.controller.BaseController;
import com.free.swd_392.core.controller.*;
import com.free.swd_392.core.model.BaseResponse;
import com.free.swd_392.dto.user.UserDetails;
import com.free.swd_392.dto.user.UserInfo;
import com.free.swd_392.dto.user.request.SystemUserPageFilter;
import com.free.swd_392.entity.user.RoleEntity;
import com.free.swd_392.entity.user.UserEntity;
import com.free.swd_392.enums.RoleKind;
import com.free.swd_392.exception.InvalidException;
import com.free.swd_392.mapper.UserMapper;
import com.free.swd_392.repository.user.RoleRepository;
import com.free.swd_392.repository.user.UserRepository;
import com.free.swd_392.shared.model.EmailModel;
import com.free.swd_392.shared.utils.JwtUtils;
import com.free.swd_392.shared.utils.PhoneUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@Tag(name = "System User Controller")
@RestController
@RequestMapping("/api/v1/system/user")
@RequiredArgsConstructor
public class SystemUserController extends BaseController implements
        ICreateModelController<String, UserDetails, UserDetails, String, UserEntity>,
        IUpdateModelController<String, UserDetails, UserDetails, String, UserEntity>,
        IDeleteModelByIdController<String, String, UserEntity>,
        IGetInfoPageWithFilterController<String, UserInfo, String, UserEntity, SystemUserPageFilter>,
        IGetDetailsController<String, UserDetails, String, UserEntity>,
        IGetDetailsByContextController<String, UserDetails, String, UserEntity> {

    @Getter
    private final UserRepository repository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final FirebaseAuth firebaseAuth;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Operation(security = @SecurityRequirement(name = "x-api-key"))
    @PostMapping("/admin/create")
    public ResponseEntity<BaseResponse<UserDetails>> createAdmin(@Valid @RequestBody UserDetails details) throws InvalidException {
        preCreateCheck(details);
        String randomPassword = RandomStringUtils.randomAlphanumeric(12);
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(details.getEmail())
                .setEmailVerified(false)
                .setPassword(randomPassword)
                .setPhoneNumber(PhoneUtils.convertPhoneNumberWithIdentifier(details.getPhone()))
                .setDisplayName(details.getName())
                .setDisabled(false);
        if (StringUtils.isNotBlank(details.getPhoto())) {
            request.setPhotoUrl(details.getPhoto());
        }
        RoleEntity roleAdmin = roleRepository.findFirstByKind(RoleKind.SUPER_ADMIN)
                .orElseThrow(() -> new InvalidException("Role super admin not found"));
        UserRecord adminRecord;
        try {
            adminRecord = firebaseAuth.createUser(request);
            firebaseAuth.setCustomUserClaims(
                    adminRecord.getUid(),
                    Map.of("resource_access", Map.of(
                                    "auction", Map.of(
                                            "roles",
                                            List.of(RoleKind.SUPER_ADMIN_VALUE)
                                    )
                            )
                    )
            );
            log.info("Create admin {} successfully with password {}", details.getEmail(), randomPassword);
        } catch (FirebaseAuthException e) {
            log.error("Firebase Auth Error:", e);
            throw new InvalidException(internalServerError());
        }
        UserEntity newAdmin = userMapper.fromUserRecordToEntity(adminRecord);
        newAdmin.setRoleId(roleAdmin.getId());
        return success(convertToDetails((repository.save(newAdmin))));
    }

    @Override
    public ResponseEntity<BaseResponse<UserDetails>> getDetailsByContext() {
        var userEntity = repository.findById(JwtUtils.getUserId())
                .orElseThrow(() -> new InvalidException(notFound()));
        return success(convertToDetails(userEntity));
    }

    @Override
    public UserDetails preCreate(UserDetails details) {
        preCreateCheck(details);
        var roleEntity = roleRepository.findById(details.getRoleId())
                .orElseThrow(() -> new InvalidException("Role id not found"));
        var randomPassword = RandomStringUtils.randomAlphanumeric(12);
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(details.getEmail())
                .setEmailVerified(false)
                .setPassword(randomPassword)
                .setPhoneNumber(PhoneUtils.convertPhoneNumberWithIdentifier(details.getPhone()))
                .setDisplayName(details.getName())
                .setDisabled(false);
        if (StringUtils.isNotBlank(details.getPhoto())) {
            request.setPhotoUrl(details.getPhoto());
        }
        UserRecord userRecord;
        try {
            userRecord = firebaseAuth.createUser(request);
            firebaseAuth.setCustomUserClaims(
                    userRecord.getUid(),
                    Map.of("resource_access", Map.of(
                                    "auction", Map.of(
                                            "roles",
                                            List.of(roleEntity.getKind().name())
                                    )
                            )
                    )
            );
            log.debug("Create admin {} successfully with password {}", details.getEmail(), randomPassword);
        } catch (FirebaseAuthException e) {
            log.error("Firebase Auth Error:", e);
            throw new InvalidException(internalServerError());
        }
        details.setId(userRecord.getUid());
        details.setPassword(randomPassword);
        return details;
    }

    private void preCreateCheck(UserDetails details) {
        if (repository.existsByEmail(details.getEmail())) {
            throw new InvalidException("Email already existed");
        }
        if (repository.existsByPhone(details.getPhone())) {
            throw new InvalidException("Email already existed");
        }
    }

    @Override
    public void postCreate(UserEntity entity, UserDetails request, UserDetails details) {
        Assert.notNull(details, "details cannot be null");
        applicationEventPublisher.publishEvent(
                EmailModel.builder()
                        .to(new String[]{entity.getEmail()})
                        .subject("[Auction SWD_392] Mat khau dang nhap")
                        .content("Mat khau dang nhap Auction SWD_392 cua ban la: " + details.getPassword())
                        .isHtml(false)
                        .hasAttachment(false)
                        .build()
        );
    }

    @Override
    public void postDelete(String id) {
        try {
            firebaseAuth.deleteUser(id);
        } catch (FirebaseAuthException e) {
            log.error("Firebase Auth Error:", e);
            throw new InvalidException(internalServerError());
        }
    }

    @Override
    public UserEntity createConvertToEntity(UserDetails details) {
        return userMapper.createConvertToEntity(details);
    }

    @Override
    public void updateConvertToEntity(UserEntity entity, UserDetails details) throws InvalidException {
        userMapper.updateConvertToEntity(entity, details);
    }

    @Override
    public UserDetails convertToDetails(UserEntity entity) {
        return userMapper.convertToDetails(entity);
    }

    @Override
    public UserInfo convertToInfo(UserEntity entity) {
        return userMapper.convertToInfo(entity);
    }

    @Override
    public String notFound() {
        return "User not found";
    }
}
