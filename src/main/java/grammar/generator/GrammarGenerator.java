package grammar.generator;

import grammar.Grammar;
import grammar.io.GrammarReader;
import grammar.GrammarTerm;

import java.io.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GrammarGenerator {

    private static final String FILE_PATH = "grammars/minijava-grammar";
    private static final String OUT_PATH = "src/main/java/syntactic/MiniJavaSyntacticAnalyzer.java";
    private static final String LAMBDA = "EOF";
    private static final GrammarTerm INITIAL = new GrammarTerm("Inicial", true);

    public static void main(String[] args) throws IOException {
        Grammar grammar = new GrammarReader(FILE_PATH, LAMBDA, new GeneratorGrammarValidator()).getGrammar();
        List<GeneratorMethod> methods = new GeneratorScraper(grammar).createMethods(INITIAL);

        StringWriter writer = new StringWriter();
        GeneratorPrinter printer = new GeneratorPrinter(new PrintWriter(writer));
        printer.print(methods);

        writeAtStart(writer.toString());
    }

    private static void writeAtStart(String generatedCode) throws IOException {
        StringBuilder out = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(OUT_PATH));
        boolean shouldSkip = false;
        String line = reader.readLine();
        while (line != null){
            if (line.contains("///End")){
                shouldSkip = false;
            }
            if (!shouldSkip){
                out.append(line).append("\n");
            }
            if (line.contains("///Start")){
                out.append(generatedCode);
                shouldSkip = true;
            }
            line = reader.readLine();
        }

        FileWriter writer = new FileWriter(OUT_PATH, false);
        writer.write(out.toString());
        writer.close();
    }

}
