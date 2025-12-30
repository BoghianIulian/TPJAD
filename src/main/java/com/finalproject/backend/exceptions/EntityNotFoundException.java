package com.finalproject.backend.exceptions;

public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(String entityName, Long id) {
        super(entityName + " with id " + id + " not found");
    }
    public EntityNotFoundException(String message) {
        super(message);
    }
}
