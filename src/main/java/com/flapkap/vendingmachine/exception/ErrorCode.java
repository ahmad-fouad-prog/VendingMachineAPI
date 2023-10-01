package com.flapkap.vendingmachine.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    RESOURCE_NOT_FOUND("Resource not found", HttpStatus.NOT_FOUND),

    USER_NOT_FOUND("User not found", HttpStatus.NOT_FOUND),
    UNAUTHORIZED_ACTION("Unauthorized action", HttpStatus.FORBIDDEN),
    USER_ALREADY_EXIST("user already exist", HttpStatus.FORBIDDEN),

    ACCESS_DENIED("ACCESS_DENIED", HttpStatus.FORBIDDEN),
    OPERATION_NOT_ALLOWED(  "OPERATION_NOT_ALLOWED",HttpStatus.UNAUTHORIZED),

    SELLER_HAS_PRODUCTS(  "Operation denied because the seller has associated products.",HttpStatus.UNAUTHORIZED),




    INVALID_USER_ROLE(  "Only users with role BUYER can deposit",HttpStatus.UNAUTHORIZED),

    INSUFFICIENT_FUNDS(  "INSUFFICIENT_FUNDS",HttpStatus.UNPROCESSABLE_ENTITY),
    OUT_OF_STOCK(  "OUT_OF_STOCK",HttpStatus.UNPROCESSABLE_ENTITY),

    INVALID_INPUT("INVALID_COIN_INPUT Valid denominations are: 0.05, 0.10, 0.20, 0.50, and 1.00", HttpStatus.BAD_REQUEST);


    private final String defaultMessage;
    private final HttpStatus httpStatus;

    ErrorCode(String defaultMessage, HttpStatus httpStatus) {
        this.defaultMessage = defaultMessage;
        this.httpStatus = httpStatus;
    }

    public String getDefaultMessage() {
        return this.defaultMessage;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
    }

