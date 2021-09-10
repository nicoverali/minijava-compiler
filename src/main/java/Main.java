import io.code.ScannerSourceCodeReader;
import io.code.SourceCodeReader;
import lexical.LexicalException;
import lexical.Token;
import lexical.analyzer.LexicalAnalyzer;
import lexical.analyzer.MiniJavaLexicalAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static final LexicalErrorPrinter errorPrinter = new LexicalErrorPrinter(System.out);

    public static void main(String[] args) throws IOException {
        if (args.length < 1) throw new IllegalArgumentException("Se debee proveer el archivo a a analizar");
        LexicalAnalyzer analyzer = createLexicalAnalyzer(args[0]);

        boolean reachEOF = false;
        do {
            try{
                Optional<Token> token = analyzer.getNextToken();
                token.ifPresent(System.out::println);
                reachEOF = token.isEmpty();
            } catch (LexicalException e){
                errorPrinter.printError(e);
            }
        } while (!reachEOF);
    }

    private static LexicalAnalyzer createLexicalAnalyzer(String filePath) throws IOException{
        Scanner scanner = new Scanner(new File(filePath));
        SourceCodeReader codeReader = new ScannerSourceCodeReader(scanner);
        return new MiniJavaLexicalAnalyzer(codeReader);
    }



}
