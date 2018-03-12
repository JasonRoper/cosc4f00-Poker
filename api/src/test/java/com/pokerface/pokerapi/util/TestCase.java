package com.pokerface.pokerapi.util;

/**
 * {@link TestCase} is a helper class for the testing code. It makes it easy to pack a input value, and an
 * expected value, with an optional method into a single class that can be easily passed around within test.
 * <p>
 * This class is useful in the following pattern.
 * <pre>{@code
 * TestCase[] testCases = new TestCase[]{
 *     new TestCase<>("a message", new InputValue(1), new ExpectedValue(2)),
 *     new TestCase<>("another test, new InputValue(2), new ExpectedValue(2))
 * }
 *
 * for(TestCase<InputValue,ExpectedValue> testCase : testCases) {
 *     assertEquals(testCase.getMessage(), testCase.getCorrectResult(), testCase.getInput().doWork());
 * }
 * }</pre>
 *
 * @param <T> The type of the input
 * @param <U> The type of the expected value
 */
public class TestCase<T, U> {
    private final String message;
    private T input;
    private U correctResult;

    /**
     * Create a new {@link TestCase} without a message
     *
     * @param input the input value
     * @param correctResult the correct result
     */
    public TestCase(T input, U correctResult) {
        this.message = "";
        this.input = input;
        this.correctResult = correctResult;
    }

    /**
     * Create a new {@link TestCase}.
     *
     * @param message the message attached to the test case
     * @param input the input value
     * @param correctResult the correct result
     */
    public TestCase(final String message, T input, U correctResult) {
        this.message = message;
        this.input= input;
        this.correctResult = correctResult;
    }

    /**
     * Get the message attached to this {@link TestCase}.
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Get the input for this {@link TestCase}
     * @return the input
     */
    public T getInput() {
        return input;
    }

    /**
     * Get the correct result for this {@link TestCase}
     * @return the correct result
     */
    public U getCorrectResult() {
        return correctResult;
    }
}
