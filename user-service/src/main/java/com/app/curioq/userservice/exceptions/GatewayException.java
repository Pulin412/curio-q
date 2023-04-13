package com.app.curioq.userservice.exceptions;

import java.io.Serial;

public class GatewayException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    public GatewayException(String msg) {
        super(msg);
    }
}
