package com.app.curioq.qaservice.exceptions;

import java.io.Serial;

public class InvalidRequestException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    public InvalidRequestException(String msg) {
        super(msg);
    }
}
