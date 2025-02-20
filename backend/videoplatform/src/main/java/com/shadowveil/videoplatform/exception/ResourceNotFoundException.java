// src/main/java/com/shadowveil/videoplatform/exception/ResourceNotFoundException.java
package com.shadowveil.videoplatform.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}