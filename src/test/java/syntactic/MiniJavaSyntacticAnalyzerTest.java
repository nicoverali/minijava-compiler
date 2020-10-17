package syntactic;

import io.code.CodeLineFactory;
import io.code.DefaultCodeLineFactory;
import io.code.reader.BufferedCodeLinesReader;
import io.code.reader.CodeLinesReader;
import io.code.reader.DefaultSourceCodeReader;
import io.code.reader.SourceCodeReader;
import lexical.analyzer.LexicalSequence;
import lexical.analyzer.MiniJavaLexicalAnalyzer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MiniJavaSyntacticAnalyzerTest {

    private static final int TEST_COUNT = 32;

    CodeLineFactory lineFactory = new DefaultCodeLineFactory();

    private static Stream<Arguments> syntacticTestFiles() {
        ClassLoader loader = MiniJavaSyntacticAnalyzer.class.getClassLoader();
        Arguments[] arguments = new Arguments[TEST_COUNT];
        for (int i = 1; i <= TEST_COUNT; i++) {
            arguments[i-1] = Arguments.of(loader.getResource("syntactic_tests/test_"+i+".java").getPath());
        }

        return Stream.of(arguments);
    }

    @ParameterizedTest
    @MethodSource("syntacticTestFiles")
    void testFiles(String filePath) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        String errorToken = getErrorToken(bufferedReader);
        SyntacticAnalyzer testSubject = getSyntacticAnalyzer(bufferedReader);

        try{
            testSubject.analyze();
            assertEquals("", errorToken); // If does not throw exception then we should expect an exception
        } catch (SyntacticException e){
            assertEquals(errorToken, e.getExceptionToken().getType().name());
        }
    }

    private MiniJavaSyntacticAnalyzer getSyntacticAnalyzer(BufferedReader bufferedReader) {
        CodeLinesReader linesReader = new BufferedCodeLinesReader(bufferedReader, lineFactory);
        SourceCodeReader reader = new DefaultSourceCodeReader(linesReader);
        MiniJavaLexicalAnalyzer lexicalAnalyzer = new MiniJavaLexicalAnalyzer(reader);
        LexicalSequence lexicalSequence = new LexicalSequence(lexicalAnalyzer);
        return new MiniJavaSyntacticAnalyzer(lexicalSequence);
    }

    private String getErrorToken(BufferedReader bufferedReader) throws IOException {
        String error = bufferedReader.readLine();
        return error != null
                ? error
                : "";
    }

}