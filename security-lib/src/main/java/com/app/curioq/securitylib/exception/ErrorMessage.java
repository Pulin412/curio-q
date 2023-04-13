package com.app.curioq.securitylib.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorMessage {

    private LocalDateTime timestamp;
    private String message;
}

