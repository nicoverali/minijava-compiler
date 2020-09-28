package io.code;

public class DefaultCodeLineFactory implements CodeLineFactory{
    @Override
    public CodeLine create(String line, int lineNumber) {
        if (line == null){
            return new DefaultCodeLine(lineNumber);
        }
        return new DefaultCodeLine(lineNumber, line);
    }
}
