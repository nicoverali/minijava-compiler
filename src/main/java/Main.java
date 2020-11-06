import io.code.CodeLineFactory;
import io.code.DefaultCodeLineFactory;
import io.code.reader.BufferedCodeLinesReader;
import io.code.reader.CodeLinesReader;
import io.code.reader.DefaultSourceCodeReader;
import io.code.reader.SourceCodeReader;
import lexical.LexicalException;
import lexical.analyzer.LexicalAnalyzer;
import lexical.analyzer.LexicalSequence;
import lexical.analyzer.MiniJavaLexicalAnalyzer;
import semantic.SemanticException;
import semantic.symbol.SymbolTable;
import syntactic.MiniJavaSyntacticAnalyzer;
import syntactic.SyntacticAnalyzer;
import syntactic.SyntacticException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    private static final LexicalErrorPrinter LEXICAL_ERROR_PRINTER = new LexicalErrorPrinter(System.out);
    private static final SyntacticErrorPrinter SYNTACTIC_ERROR_PRINTER = new SyntacticErrorPrinter(System.out);
    private static final SemanticErrorPrinter SEMANTIC_ERROR_PRINTER_ERROR_PRINTER = new SemanticErrorPrinter(System.out);
    private static final SemanticSuccessPrinter SUCCESS_PRINTER = new SemanticSuccessPrinter(System.out);


    public static void main(String[] args) throws IOException {
        SyntacticAnalyzer syntacticAnalyzer = createSyntacticAnalyzer("test.java");//args[0]);

        try {
            syntacticAnalyzer.analyze();
            SymbolTable.getInstance().consolidate();
            SUCCESS_PRINTER.printSuccess();
        } catch (LexicalException le) {
            LEXICAL_ERROR_PRINTER.printError(le);
        } catch (SyntacticException se) {
            SYNTACTIC_ERROR_PRINTER.printError(se);
        } catch (SemanticException se) {
            SEMANTIC_ERROR_PRINTER_ERROR_PRINTER.printError(se);
        }
    }

    private static SyntacticAnalyzer createSyntacticAnalyzer(String filePath) throws IOException{
        BufferedReader buffer = new BufferedReader(new FileReader(filePath));
        CodeLineFactory lineFactory = new DefaultCodeLineFactory();
        CodeLinesReader codeLinesReader = new BufferedCodeLinesReader(buffer, lineFactory);
        SourceCodeReader codeReader = new DefaultSourceCodeReader(codeLinesReader);

        LexicalAnalyzer minijavaLexical = new MiniJavaLexicalAnalyzer(codeReader);
        LexicalSequence lexicalSequence = new LexicalSequence(minijavaLexical);

        return new MiniJavaSyntacticAnalyzer(lexicalSequence);
    }
}
