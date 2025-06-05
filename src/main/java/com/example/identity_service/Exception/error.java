package com.example.identity_service.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum error {
    SUCCESS(1000,"Success", HttpStatus.ACCEPTED),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    UNCATEGORIZED_EXCEPTION(9999,"Uncategorized error exception",HttpStatus.INTERNAL_SERVER_ERROR),//exception khac ngoai nhung exception da dc defind
    USER_EXISTED(1002,"User existed",HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003,"Username is invalid",HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1004,"Unauthenticated",HttpStatus.UNAUTHORIZED), // who are you?->xac thuc
    UNAUTHORIZED(1005,"You don't have permission",HttpStatus.FORBIDDEN),// PHAN QUYEN (acess dinied exception)
    INVALID_DOB(1006,"Your dob must at least {min}", HttpStatus.BAD_REQUEST)
    ;

    error(int code, String message,HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    private HttpStatusCode httpStatusCode;
    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
