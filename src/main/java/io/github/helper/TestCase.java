package io.github.helper;

public class TestCase {
    private final String input;
    private final String expectedOutput;
    private final boolean isSecret;

    public TestCase(String input, String expectedOutput, boolean isSecret) {
        this.input = input;
        this.expectedOutput = expectedOutput;
        this.isSecret = isSecret;
    }

    public String getInput() { return input; }
    public String getExpectedOutput() { return expectedOutput; }
    public boolean isSecret() { return isSecret; }
}