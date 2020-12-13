package com.nyl.enrollee.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotAvailableException extends  RuntimeException {
    private String resName;
    private String fieldName;
    private Object fieldValue;

    public ResourceNotAvailableException(String resName, String fieldName, String fieldValue) {
        super(String.format("%s not found by %s : '%s'", resName, fieldName, fieldValue));
        this.resName = resName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getResName() {
        return resName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

}
