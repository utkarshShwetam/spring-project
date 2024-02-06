package com.vibrant.vibranium.Commons.ExceptionsHandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ExceptionsHandler extends Exception {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class ResourceNotFound extends RuntimeException{
        public ResourceNotFound(String message){
            super(message);
        }
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    public static class DuplicateResourceFound extends RuntimeException{
        public DuplicateResourceFound(String message){
            super(message);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class BadRequestFound extends RuntimeException{
        public BadRequestFound(String message){
            super(message);
        }
    }
}
