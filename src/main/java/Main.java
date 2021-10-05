import error.LexicalErrorPrinter;
import error.SyntacticErrorPrinter;
import io.code.ScannerSourceCodeReader;
import io.code.SourceCodeReader;
import lexical.LexicalException;
import lexical.LexicalSequence;
import lexical.analyzer.LexicalAnalyzer;
import lexical.analyzer.MiniJavaLexicalAnalyzer;
import syntactic.MiniJavaSyntacticAnalyzer;
import syntactic.SyntacticAnalyzer;
import syntactic.SyntacticException;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Scanner;

public class Main {

    private static final LexicalErrorPrinter lexicalPrinter = new LexicalErrorPrinter();
    private static final SyntacticErrorPrinter syntacticPrinter = new SyntacticErrorPrinter();

    public static void main(String[] args) {
        if (args.length < 1) throw new IllegalArgumentException("Se debee proveer el archivo a a analizar");
        LexicalAnalyzer lexical = createLexicalAnalyzer(args[0]);
        SyntacticAnalyzer syntactic = createSyntacticAnalyzer(lexical);

        boolean isOk = true;
        try {
            syntactic.analyze();
        } catch (LexicalException e) {
            lexicalPrinter.printError(e);
            isOk = false;
        } catch (SyntacticException e) {
            syntacticPrinter.printError(e);
            isOk = false;
        }

        if (isOk){
            System.out.println("Compilacion exitosa.\n");
            System.out.println("[SinErrores]");
        }
    }

    private static LexicalAnalyzer createLexicalAnalyzer(String filePath){
        try {
            Scanner scanner = new Scanner(new File(filePath));
            SourceCodeReader codeReader = new ScannerSourceCodeReader(scanner);
            return new MiniJavaLexicalAnalyzer(codeReader);
        } catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

    private static SyntacticAnalyzer createSyntacticAnalyzer(LexicalAnalyzer analyzer){
        LexicalSequence sequence = new LexicalSequence(analyzer);
        return new MiniJavaSyntacticAnalyzer(sequence);
    }



}
