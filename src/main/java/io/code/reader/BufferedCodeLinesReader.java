package io.code.reader;

import io.code.CodeLine;
import io.code.CodeLineFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Optional;

public class BufferedCodeLinesReader implements CodeLinesReader {

    private final BufferedReader buffReader;
    private final CodeLineFactory lineFactory;

    private int currentLineNumber;
    private CodeLine currentLine;
    private String nextLine;


    public BufferedCodeLinesReader(BufferedReader bufferedReader, CodeLineFactory lineFactory) throws UncheckedIOException{
        this.buffReader = bufferedReader;
        this.lineFactory = lineFactory;

        initFirstLines();
    }

    private void initFirstLines() throws UncheckedIOException{
        try{
            nextLine = buffReader.readLine();
            moveToNextLine();
        } catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public Optional<CodeLine> next() throws UncheckedIOException {
        CodeLine returnLine = currentLine;
        moveToNextLine();
        return Optional.ofNullable(returnLine);
    }

    @Override
    public boolean hasNext() {
        return currentLine != null;
    }

    @Override
    public Optional<CodeLine> peek() {
        return Optional.ofNullable(currentLine);
    }

    private void moveToNextLine() throws UncheckedIOException{
        try {
            if (nextLine == null){
                currentLine = null;
                return;
            }
            currentLine = lineFactory.create(nextLine, currentLineNumber++);
            nextLine = buffReader.readLine();
            if (nextLine != null){
                currentLine.addLineSeparator();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
