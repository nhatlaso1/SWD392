package com.free.swd_392.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.free.swd_392.core.view.View;
import com.free.swd_392.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonView(View.Info.class)
public class UserDetails extends UserInfo {

    private Gender gender;
    @PastOrPresent
    private LocalDate birthday;
    @JsonIgnore
    @Schema(hidden = true)
    private String password;
}
