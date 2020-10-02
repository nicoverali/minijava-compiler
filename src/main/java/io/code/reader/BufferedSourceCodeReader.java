package io.code.reader;

import io.code.CodeCharacter;
import io.code.CodeLine;
import io.code.CodeLineFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.ListIterator;
import java.util.Optional;

public class BufferedSourceCodeReader implements SourceCodeReader {

    private BufferedReader buffReader;
    private CodeLineFactory lineFactory;

    private int currentLineNumber;

    private CodeLine currentLine;
    private ListIterator<CodeCharacter> currentLineIterator;

    private String nextLineString;


    /**
     * Creates a new SourceCodeReader that will read characters from the given {@link BufferedReader}.
     * <br>
     * It will assume that the given <code>reader</code> is at the correct position.
     *
     * @param reader a {@link BufferedReader} to read characters from
     * @param factory a {@link CodeLineFactory} to generate the source code lines
     * @throws UncheckedIOException if an I/O error occur
     */
    public BufferedSourceCodeReader(BufferedReader reader, CodeLineFactory factory) throws UncheckedIOException {
        buffReader = reader;
        lineFactory = factory;

        try{
            initFirstLines();
        } catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

    private void initFirstLines() throws IOException{
        currentLineIterator = Collections.emptyListIterator();
        currentLineNumber = 0;

        nextLineString = buffReader.readLine();
        if (!hasReachEndOfFile()){
            moveToNextLine();
        }
    }

    @Override
    public Optional<CodeCharacter> getNext() throws UncheckedIOException {
        if (currentLineIterator.hasNext()){
            return Optional.of(currentLineIterator.next());
        } else if (hasReachEndOfFile()){
            return Optional.empty();
        } else {
            try{
                moveToNextLine();
                return getNext();
            } catch (IOException e){
                throw new UncheckedIOException(e);
            }
        }
    }

    private void moveToNextLine() throws IOException{
        currentLine = lineFactory.create(nextLineString, currentLineNumber);
        currentLineNumber++;

        nextLineString = buffReader.readLine();
        if (nextLineString != null){
            currentLine.addLineSeparator();
        }

        currentLineIterator = currentLine.iterator();
    }

    private boolean hasReachEndOfFile(){
        return !currentLineIterator.hasNext() && nextLineString == null;
    }

    @Override
    public boolean hasNext() {
        return !hasReachEndOfFile();
    }

    @Override
    public Optional<CodeCharacter> peekNext() {
        if (currentLineIterator.hasNext()){
            CodeCharacter nextCharacter = currentLine.getCharacterAt(currentLineIterator.nextIndex());
            return Optional.of(nextCharacter);
        }
        return lineFactory.create(nextLineString, currentLineNumber+1).getFirstCharacter();
    }

    @Override
    public Optional<CodeLine> getCurrentLine() {
        return Optional.ofNullable(currentLine);
    }

    @Override
    public int getCurrentLineNumber() {
        if (currentLine != null){
            return currentLine.getLineNumber();
        }
        return 0;
    }

}
