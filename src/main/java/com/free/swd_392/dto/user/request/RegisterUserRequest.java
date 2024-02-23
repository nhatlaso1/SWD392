package com.free.swd_392.dto.user.request;

import com.free.swd_392.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterUserRequest {

    @Size(min = 1, max = 50)
    @NotBlank
    @Schema(example = "Nguyen Van A")
    private String name;
    @NotBlank
    @Schema(example = "Thu Duc City, Ho Chi Minh City")
    private String address;
    private LocalDate dateOfBirth;
    @NotNull
    private Gender gender;
}
