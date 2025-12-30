package com.finalproject.backend.exceptions;

public class EntityIdMismatchException extends RuntimeException {

    public EntityIdMismatchException(String entityName, Long pathId, Long bodyId) {
        super(entityName + " ID mismatch: path ID = " + pathId + ", body ID = " + bodyId);
    }
}
