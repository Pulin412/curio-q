package com.app.curioq.userservice.exceptions;

import java.io.Serial;

public class InvalidLoginException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    public InvalidLoginException(String msg) {
        super(msg);
    }
}
