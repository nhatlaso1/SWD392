package com.free.swd_392.dto.error;

import com.free.swd_392.dto.common.BaseResponse;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Data
@Accessors(chain = true)
@SuperBuilder(toBuilder = true)
public class ErrorResponse extends BaseResponse<Object> {

    private String message;
}
