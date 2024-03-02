package com.free.swd_392.config.security.exception;

import lombok.experimental.StandardException;
import org.springframework.security.core.AuthenticationException;

@StandardException
public class DefaultAuthenticationException extends AuthenticationException {

}
