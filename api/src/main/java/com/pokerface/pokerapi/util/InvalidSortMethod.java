package com.pokerface.pokerapi.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidSortMethod extends RuntimeException {
    private String attempt;
    private String[] methods;

    public InvalidSortMethod(String attempt, String ...methods) {
        super("the sort method \'" + attempt + "\' is not a valid sort method. valid methods: " + Arrays.toString(methods));
        this.attempt = attempt;
        this.methods = methods;

    }
}
