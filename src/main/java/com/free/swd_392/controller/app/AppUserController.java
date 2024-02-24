package com.free.swd_392.controller.app;

import com.free.swd_392.controller.BaseController;
import com.free.swd_392.core.controller.IGetDetailsByContextController;
import com.free.swd_392.core.controller.IUpdateModelController;
import com.free.swd_392.core.model.BaseResponse;
import com.free.swd_392.dto.user.UserDetails;
import com.free.swd_392.entity.user.UserEntity;
import com.free.swd_392.exception.InvalidException;
import com.free.swd_392.mapper.UserMapper;
import com.free.swd_392.repository.user.UserRepository;
import com.free.swd_392.shared.utils.JwtUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "App User Controller")
@RestController
@RequestMapping("/api/v1/app/user")
@RequiredArgsConstructor
public class AppUserController extends BaseController implements
        IGetDetailsByContextController<String, UserDetails, String, UserEntity>,
        IUpdateModelController<String, UserDetails, String, UserEntity> {

    private final UserMapper userMapper;
    private final UserRepository repository;

    @Override
    public ResponseEntity<BaseResponse<UserDetails>> getDetailsByContext() {
        var userEntity = repository.findById(JwtUtils.getUserId())
                .orElseThrow(() -> new InvalidException(notFound()));
        return success(convertToDetails(userEntity));
    }

    @Override
    public void updateConvertToEntity(UserEntity entity, UserDetails details) throws InvalidException {
        userMapper.updateConvertToEntityApp(entity, details);
    }

    @Override
    public UserDetails convertToDetails(UserEntity entity) {
        return userMapper.convertToDetail(entity);
    }

    @Override
    public JpaRepository<UserEntity, String> getRepository() {
        return repository;
    }
}
