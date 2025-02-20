// src/main/java/com/shadowveil/videoplatform/exception/BadRequestException.java
package com.shadowveil.videoplatform.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}