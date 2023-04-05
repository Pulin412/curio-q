package com.app.curioq.userservice.userservice.utils;

import java.time.format.DateTimeFormatter;

public class UserServiceConstants {
    public static final int TOKEN_PREFIX_LENGTH = "Bearer ".length();
    public static final String VALIDATION_EMAIL_REGEX = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
    public static final DateTimeFormatter CUSTOM_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    public static final String EXCEPTION_INVALID_LOGIN_MESSAGE = "Invalid Login Details";
    public static final String EXCEPTION_USER_ALREADY_PRESENT_MESSAGE = "Invalid Login Details";
    public static final String EXCEPTION_INVALID_FIRST_NAME_MESSAGE = "First Name missing";
    public static final String EXCEPTION_INVALID_EMAIL_MESSAGE = "Invalid Email";
    public static final String EXCEPTION_INVALID_PASSWORD_MESSAGE = "Password not entered";
}
