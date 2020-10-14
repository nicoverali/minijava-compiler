package grammar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GrammarReader {

    private final String filePath;
    private final String lambda;

    public GrammarReader(String filePath, String lambda){
        this.filePath = filePath;
        this.lambda = lambda;
    }

    public Grammar getGrammar() throws UncheckedIOException {
        try{
            String[] lines = readGrammarFile();
            return parseGrammar(lines);
        } catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

    private String[] readGrammarFile() throws IOException {
        return new BufferedReader(new FileReader(filePath)).lines().toArray(String[]::new);
    }

    private Grammar parseGrammar(String[] lines){
        Grammar grammar = new Grammar(lambda);
        for (String line : lines){
            String[] lineArray = line.split("\\s+");
            String head = lineArray[0];
            String[] linesBodies = Arrays.copyOfRange(lineArray, 2, lineArray.length);

            List<List<String>> allBodies = new ArrayList<>();
            List<String> currentBody = new ArrayList<>();
            for (String term : linesBodies){
                if (!term.equals("|")){
                    currentBody.add(term);
                }else {
                    allBodies.add(currentBody);
                    currentBody = new ArrayList<>();
                }
            }
            allBodies.add(currentBody);
            grammar.addAll(head, allBodies);
        }
        return grammar;
    }
}
