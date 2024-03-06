package com.free.swd_392.dto.merchant;

import com.free.swd_392.dto.user.UserDetails;
import lombok.Data;

@Data
public class MerchantDetails extends MerchantInfo {

    private String representativeName;
    private String representativeEmail;
    private String representativePhone;
    private String idFront;
    private String idBack;
    private UserDetails owner;
}
