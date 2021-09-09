package io.code;

import sequence.ListMarkableSequence;

import java.io.UncheckedIOException;
import java.util.Optional;
import java.util.Scanner;

public class ScannerSourceCodeReader implements SourceCodeReader {

    private final ListMarkableSequence<CodeCharacter> characters = new ListMarkableSequence<>();
    private final Scanner fileScanner;

    private int nextLineNumber = 0;
    private CodeCharacter lastCharacter = null;

    public ScannerSourceCodeReader(Scanner fileScanner){
        this.fileScanner = fileScanner;
    }

    @Override
    public Optional<CodeCharacter> next() throws UncheckedIOException {
        if (!characters.hasNext() && fileScanner.hasNextLine()){
            characters.appendAll(createNextLine());
        }
        lastCharacter = characters.peek().orElse(lastCharacter);
        return characters.next();
    }

    @Override
    public boolean hasNext() throws UncheckedIOException {
        return characters.hasNext() || fileScanner.hasNextLine();
    }

    @Override
    public Optional<CodeCharacter> peek() throws UncheckedIOException {
        if (!characters.hasNext() && fileScanner.hasNextLine()){
            characters.appendAll(createNextLine());
        }
        return characters.peek();
    }

    @Override
    public Optional<CodeLine> getLastLine() {
        return lastCharacter != null
                ? Optional.of(lastCharacter.getCodeLine())
                : Optional.empty();
    }

    @Override
    public Optional<CodeCharacter> getLastCharacter() {
        return Optional.ofNullable(lastCharacter);
    }

    @Override
    public int getCurrentLineNumber() {
        return lastCharacter != null
                ? lastCharacter.getLineNumber()
                : 0;
    }

    @Override
    public void mark(int readAheadLimit) throws IllegalArgumentException {
        characters.mark(readAheadLimit);
    }

    @Override
    public void flushMarkers() {
        characters.flushMarkers();
    }

    @Override
    public void reset() throws IllegalStateException {
        characters.reset();
    }

    private CodeLine createNextLine(){
        String line = fileScanner.nextLine();
        if (fileScanner.hasNextLine() || line.isEmpty()){
            line += '\n';
        }
        return new DefaultCodeLine(nextLineNumber++, line);
    }
}
