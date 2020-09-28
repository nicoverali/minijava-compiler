package io.code;

public class DefaultCodeLineTest implements CodeLineTest{

    @Override
    public CodeLine createCodeLine(String line, int lineNumber) {
        return new DefaultCodeLineFactory().create(line, lineNumber);
    }

}
