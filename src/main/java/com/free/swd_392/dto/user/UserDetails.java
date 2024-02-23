package com.free.swd_392.dto.user;

import com.free.swd_392.enums.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDetails extends UserInfo {

    private Gender gender;
    private LocalDate birthday;
}
