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
import syntactic.MiniJavaSyntacticAnalyzer;
import syntactic.SyntacticAnalyzer;
import syntactic.SyntacticException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    private static final LexicalErrorPrinter LEXICAL_ERROR_PRINTER = new LexicalErrorPrinter(System.out);
    private static final SyntacticErrorPrinter ERROR_PRINTER = new SyntacticErrorPrinter(System.out);
    private static final SyntacticSuccessPrinter SUCCESS_PRINTER = new SyntacticSuccessPrinter(System.out);


    public static void main(String[] args) throws IOException {
        SyntacticAnalyzer syntacticAnalyzer = createSyntacticAnalyzer(args[0]);

        try {
            syntacticAnalyzer.analyze();
            SUCCESS_PRINTER.printSuccess();
        } catch (LexicalException le) {
            LEXICAL_ERROR_PRINTER.printError(le);
        } catch (SyntacticException se) {
            ERROR_PRINTER.printError(se);
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
