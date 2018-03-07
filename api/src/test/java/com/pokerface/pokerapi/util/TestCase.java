package com.pokerface.pokerapi.util;

public class TestCase<T, U> {
    private final String message;
    private T input;
    private U correctResult;

    public TestCase(T input, U correctResult) {
        this.message = "";
        this.input = input;
        this.correctResult = correctResult;
    }

    public TestCase(final String message, T input, U correctResult) {
        this.message = message;
        this.input= input;
        this.correctResult = correctResult;
    }

    public String getMessage() {
        return message;
    }

    public T getInput() {
        return input;
    }

    public U getCorrectResult() {
        return correctResult;
    }
}
