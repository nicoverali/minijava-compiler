import error.*;
import io.code.ScannerSourceCodeReader;
import io.code.SourceCodeReader;
import lexical.LexicalException;
import lexical.LexicalSequence;
import lexical.analyzer.LexicalAnalyzer;
import lexical.analyzer.MiniJavaLexicalAnalyzer;
import semantic.CircularInheritanceException;
import semantic.MainMethodException;
import semantic.SemanticException;
import semantic.symbol.SymbolTable;
import syntactic.MiniJavaSyntacticAnalyzer;
import syntactic.SyntacticAnalyzer;
import syntactic.SyntacticException;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

public class Main {

    private static final LexicalErrorPrinter lexicalPrinter = new LexicalErrorPrinter();
    private static final SyntacticErrorPrinter syntacticPrinter = new SyntacticErrorPrinter();
    private static final SemanticErrorPrinter semanticPrinter = new SemanticErrorPrinter();
    private static final CircularInheritanceErrorPrinter circularPrinter = new CircularInheritanceErrorPrinter();
    private static final MainMethodErrorPrinter mainPrinter = new MainMethodErrorPrinter();

    public static void main(String[] args) {
        if (args.length < 1) throw new IllegalArgumentException("Se debee proveer el archivo a analizar");
        LexicalAnalyzer lexical = createLexicalAnalyzer(args[0]);
        SyntacticAnalyzer syntactic = createSyntacticAnalyzer(lexical);

        boolean isOk = false;
        try {
            syntactic.analyze();
            SymbolTable.getInstance().consolidate();
            isOk = true;
        } catch (LexicalException e) {
            lexicalPrinter.printError(e);
        } catch (SyntacticException e) {
            syntacticPrinter.printError(e);
        } catch (SemanticException e) {
            semanticPrinter.printError(e);
        } catch (CircularInheritanceException e) {
            circularPrinter.printError(e);
        } catch (MainMethodException e) {
            mainPrinter.printError(e);
        }

        if (isOk){
            System.out.println("Compilacion exitosa.\n");
            System.out.println("[SinErrores]");
        }
    }

    private static LexicalAnalyzer createLexicalAnalyzer(String filePath){
        try {
            Scanner scanner = new Scanner(new File(filePath), ISO_8859_1);
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
