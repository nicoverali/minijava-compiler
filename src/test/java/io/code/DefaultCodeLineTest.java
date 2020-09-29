package io.code;

public class DefaultCodeLineTest implements CodeLineTest<DefaultCodeLine> {

    @Override
    public DefaultCodeLine createCodeLine(String line, int lineNumber) {
        return new DefaultCodeLine(lineNumber, line);
    }

}
