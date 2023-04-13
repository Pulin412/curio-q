package com.app.curioq.userservice.exceptions;

import java.io.Serial;

public class UserAlreadyPresentException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    public UserAlreadyPresentException(String msg) {
        super(msg);
    }
}
