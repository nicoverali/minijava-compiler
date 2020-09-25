package io.code;

public class DefaultCodeLineFactory implements CodeLineFactory{

    private int lineNumber = 0;

    @Override
    public CodeLine create(String line) {
        return new DefaultCodeLine(++lineNumber, line);
    }
}
