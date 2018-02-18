package com.pokerface.pokerapi.util;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class BadRequestError extends RESTError {
    private List<ErrorPair> invalidFields;


        public BadRequestError(String type) {
        super(HttpStatus.BAD_REQUEST, type);
        this.invalidFields = new ArrayList<>();
    }

    public void addFieldError(String field, String reason) {
        invalidFields.add(new ErrorPair(field, reason));
    }

    public int getNumberErrors() {
        return this.invalidFields.size();
    }
}
