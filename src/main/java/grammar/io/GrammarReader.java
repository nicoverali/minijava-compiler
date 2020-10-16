package grammar.io;

import grammar.Grammar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;

public class GrammarReader {

    private final GrammarParser parser;

    private final String filePath;
    private final String lambda;

    public GrammarReader(String filePath, String lambda, GrammarValidator validator){
        this.filePath = filePath;
        this.lambda = lambda;
        this.parser = new GrammarParser(validator);
    }

    public Grammar getGrammar() throws UncheckedIOException {
        try{
            String[] lines = readGrammarFile();
            return parser.parse(lines, lambda);
        } catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

    private String[] readGrammarFile() throws IOException {
        return new BufferedReader(new FileReader(filePath)).lines().toArray(String[]::new);
    }
}
