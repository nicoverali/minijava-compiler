package io.code.reader;

import io.code.CodeCharacter;
import io.code.CodeLine;
import sequence.ListMarkableSequence;

import java.io.UncheckedIOException;
import java.util.Optional;

public class DefaultSourceCodeReader implements SourceCodeReader {

    private final ListMarkableSequence<CodeCharacter> characters;
    private final CodeLinesReader linesReader;

    private CodeLine currentLine;

    public DefaultSourceCodeReader(CodeLinesReader linesReader){
        this.linesReader = linesReader;
        characters = new ListMarkableSequence<>();
    }

    @Override
    public Optional<CodeCharacter> next() throws UncheckedIOException {
        if (characters.hasNext()){
            return characters.next();
        } else if (linesReader.hasNext()){
            //noinspection OptionalGetWithoutIsPresent (checked with hasNext)
            currentLine = linesReader.next().get();
            characters.appendAll(currentLine);
            return next();
        }
        return Optional.empty();
    }

    @Override
    public boolean hasNext() throws UncheckedIOException {
        return characters.hasNext() || linesReader.peek().flatMap(CodeLine::getFirstCharacter).isPresent();
    }

    @Override
    public Optional<CodeCharacter> peek() throws UncheckedIOException {
        if (characters.hasNext()){
            return characters.peek();
        }
        return linesReader.peek()
                .flatMap(CodeLine::getFirstCharacter);
    }

    @Override
    public Optional<CodeLine> getCurrentLine() {
        return Optional.ofNullable(currentLine);
    }

    @Override
    public int getCurrentLineNumber() {
        return currentLine != null
                ? currentLine.getLineNumber()
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
}
