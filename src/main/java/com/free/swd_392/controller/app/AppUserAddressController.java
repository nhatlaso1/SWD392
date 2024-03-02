package com.free.swd_392.controller.app;

import com.free.swd_392.controller.BaseController;
import com.free.swd_392.core.controller.ICreateModelController;
import com.free.swd_392.core.controller.IDeleteModelByIdController;
import com.free.swd_392.core.controller.IGetInfoListWithFilterController;
import com.free.swd_392.core.controller.IUpdateModelController;
import com.free.swd_392.core.model.SuccessResponse;
import com.free.swd_392.dto.user.UserAddressInfo;
import com.free.swd_392.dto.user.request.AppUserAddressFilter;
import com.free.swd_392.dto.user.request.CreateUserAddressRequest;
import com.free.swd_392.dto.user.request.UpdateUserAddressRequest;
import com.free.swd_392.entity.user.UserAddressEntity;
import com.free.swd_392.exception.InvalidException;
import com.free.swd_392.mapper.app.AppUserAddressMapper;
import com.free.swd_392.repository.location.LocationRepository;
import com.free.swd_392.repository.user.UserAddressRepository;
import com.free.swd_392.service.PermissionService;
import com.free.swd_392.shared.utils.JwtUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "App User Address Controller")
@RestController
@RequestMapping("/api/v1/app/user-address")
@RequiredArgsConstructor
public class AppUserAddressController extends BaseController implements
        ICreateModelController<Long, UserAddressInfo, CreateUserAddressRequest, Long, UserAddressEntity>,
        IUpdateModelController<Long, UserAddressInfo, UpdateUserAddressRequest, Long, UserAddressEntity>,
        IDeleteModelByIdController<Long, Long, UserAddressEntity>,
        IGetInfoListWithFilterController<Long, UserAddressInfo, Long, UserAddressEntity, AppUserAddressFilter> {

    private final UserAddressRepository repository;
    private final LocationRepository locationRepository;
    private final AppUserAddressMapper appUserAddressMapper;
    private final PermissionService permissionService;

    @Transactional
    @PutMapping("/default/{id}")
    public ResponseEntity<SuccessResponse> changeDefault(@PathVariable("id") @NotNull Long id) {
        if (!permissionService.isOwner(id, repository)) {
            throw new AccessDeniedException("");
        }
        repository.updateUserAddressDefault(JwtUtils.getUserId(), id);
        return success();
    }

    @Override
    public CreateUserAddressRequest preCreate(CreateUserAddressRequest request) {
        if (!locationRepository.existsLocationPath(request.getProvinceId(), request.getDistrictId(), request.getWardId())) {
            throw new InvalidException("Invalid location path");
        }
        if (repository.countByCreatedBy(JwtUtils.getUserId()) == 0) {
            request.setDefault(true);
        }
        return request;
    }

    @Override
    public void postCreate(UserAddressEntity entity, CreateUserAddressRequest request, UserAddressInfo details) {
        if (request.isDefault()) {
            repository.updateUserAddressDefault(JwtUtils.getUserId(), entity.getId());
        }
    }

    @Override
    public UpdateUserAddressRequest preUpdate(UpdateUserAddressRequest request) {
        if (!locationRepository.existsLocationPath(request.getProvinceId(), request.getDistrictId(), request.getWardId())) {
            throw new InvalidException("Invalid location path");
        }
        return request;
    }

    @Override
    public void postUpdate(UserAddressEntity entity, UpdateUserAddressRequest request, UserAddressInfo details) {
        if (request.isDefault()) {
            repository.updateUserAddressDefault(JwtUtils.getUserId(), entity.getId());
        }
    }

    @Override
    public void preDelete(Long id) {
        if (!permissionService.isOwner(id, repository)) {
            throw new AccessDeniedException("");
        }
        if (repository.existsByIdAndIsDefaultIsTrue(id)) {
            throw new InvalidException("The customer's address cannot be deleted because it is the default address");
        }
    }

    @Override
    public UserAddressInfo convertToDetails(UserAddressEntity entity) {
        return appUserAddressMapper.convertToDetails(entity);
    }

    @Override
    public UserAddressInfo convertToInfo(UserAddressEntity entity) {
        return appUserAddressMapper.convertToInfo(entity);
    }

    @Override
    public UserAddressEntity createConvertToEntity(CreateUserAddressRequest request) {
        return appUserAddressMapper.createConvertToEntity(request);
    }

    @Override
    public void updateConvertToEntity(UserAddressEntity entity, UpdateUserAddressRequest request) {
        if (!permissionService.isOwner(entity)) {
            throw new AccessDeniedException("");
        }
        appUserAddressMapper.updateConvertToEntity(entity, request);
    }

    @Override
    public JpaRepository<UserAddressEntity, Long> getRepository() {
        return repository;
    }

    @Override
    public String notFound() {
        return "Customer address not found";
    }
}
