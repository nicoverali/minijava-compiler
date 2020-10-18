package grammar.generator;

import grammar.Grammar;
import grammar.io.GrammarReader;
import grammar.GrammarTerm;

import java.io.IOException;
import java.util.List;

public class GrammarGenerator {

    private static final String FILE_PATH = "grammars/minijava-grammar-inline-assign";
    private static final String LAMBDA = "EOF";
    private static final GrammarTerm INITIAL = new GrammarTerm("Inicial", true);

    public static void main(String[] args) throws IOException {
        Grammar grammar = new GrammarReader(FILE_PATH, LAMBDA, new GeneratorGrammarValidator()).getGrammar();
        List<GeneratorMethod> methods = new GeneratorScraper(grammar).createMethods(INITIAL);

        GeneratorPrinter printer = new GeneratorPrinter("GrammarAnalyzer.java");
        printer.print(methods);
    }

}
