package io.code.reader;

import io.code.CodeCharacter;
import io.code.CodeLine;
import io.code.CodeLineFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class DefaultSourceCodeReader implements SourceCodeReader {

    private BufferedReader bufferedReader;

    private CodeLineFactory lineFactory;
    private Iterator<CodeCharacter> currentCodeLineIterator;
    private CodeLine currentCodeLine;
    private String nextLine;

    /**
     *
     * @param path path to the source code, either relative or absolute
     * @throws FileNotFoundException if the source code file could not be found
     * @throws IOException if an I/O error occur
     */
    public DefaultSourceCodeReader(String path, CodeLineFactory factory) throws FileNotFoundException, IOException {
        lineFactory = factory;
        bufferedReader = new BufferedReader(new FileReader(path));
        nextLine = bufferedReader.readLine();
        readNextLine();
    }

    private void readNextLine() throws IOException {
        String currentLine = nextLine;
        nextLine = bufferedReader.readLine();
        if (nextLine != null){
            currentLine += '\n';
        }
        currentCodeLine = lineFactory.create(currentLine);
        currentCodeLineIterator = currentCodeLine.iterator();
    }

    private boolean hasReachedEndOfFile(){
        return nextLine == null;
    }

    @Override
    public CodeCharacter getNextCharacter() throws IOException{
        if(currentCodeLineIterator.hasNext()){
            return currentCodeLineIterator.next();
        } else if (hasReachedEndOfFile()){
            bufferedReader.close();
            return null;
        } else {
            readNextLine();
            return currentCodeLineIterator.next();
        }
    }

    @Override
    public CodeLine getCurrentLine() {
        return currentCodeLine;
    }
}
