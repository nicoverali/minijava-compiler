import io.code.CodeLineFactory;
import io.code.DefaultCodeLineFactory;
import io.code.reader.BufferedCodeLinesReader;
import io.code.reader.CodeLinesReader;
import io.code.reader.DefaultSourceCodeReader;
import io.code.reader.SourceCodeReader;
import lexical.LexicalException;
import lexical.Token;
import lexical.TokenType;
import lexical.analyzer.LexicalAnalyzer;
import lexical.analyzer.MiniJavaLexicalAnalyzer;
import util.Characters;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

public class Main {

    private static final TokenPrinter tokenPrinter = new TokenPrinter(System.out);
    private static final LexicalErrorPrinter errorPrinter = new LexicalErrorPrinter(System.out);

    public static void main(String[] args) throws IOException {
        LexicalAnalyzer analyzer = createLexicalAnalyzer("test.minijava");//args[0]);

        Optional<Token> token = Optional.empty();
        do {
            try{
                token = analyzer.getNextToken();
                token.ifPresent(tokenPrinter::print);
            } catch (LexicalException e){
                errorPrinter.printError(e);
            }
        } while (token.isPresent());
    }

    private static LexicalAnalyzer createLexicalAnalyzer(String filePath) throws IOException{
        BufferedReader buffer = new BufferedReader(new FileReader(filePath));
        CodeLineFactory lineFactory = new DefaultCodeLineFactory();
        CodeLinesReader codeLinesReader = new BufferedCodeLinesReader(buffer, lineFactory);
        SourceCodeReader codeReader = new DefaultSourceCodeReader(codeLinesReader);
        return new MiniJavaLexicalAnalyzer(codeReader);
    }



}
