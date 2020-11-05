package semantic;

import io.code.CodeLineFactory;
import io.code.DefaultCodeLineFactory;
import io.code.reader.BufferedCodeLinesReader;
import io.code.reader.CodeLinesReader;
import io.code.reader.DefaultSourceCodeReader;
import io.code.reader.SourceCodeReader;
import lexical.Token;
import lexical.analyzer.LexicalSequence;
import lexical.analyzer.MiniJavaLexicalAnalyzer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import semantic.symbol.SymbolTable;
import syntactic.MiniJavaSyntacticAnalyzer;
import syntactic.SyntacticAnalyzer;
import syntactic.SyntacticException;
import util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class MiniJavaSemanticAnalyzerTest {

    private static final String BASE = "semantic_tests/";
    private static final List<Pair<String, Integer>> paths = new ArrayList<>();
    static {
        paths.add(Pair.of(BASE+"duplicates", 11));
        paths.add(Pair.of(BASE+"constructor", 3));
        paths.add(Pair.of(BASE+"simple_references", 11));
        paths.add(Pair.of(BASE+"generic_references", 19));

    }


    private static Stream<Arguments> semanticTestFiles() {
        Arguments[] arguments = paths.stream().flatMap(pair -> IntStream.range(1, pair.second+1)
                .mapToObj(fileNumber -> load(pair.first, fileNumber))
        ).toArray(Arguments[]::new);

        return Stream.of(arguments);
    }

    private static Arguments load(String path, int fileNumber){
        ClassLoader loader = MiniJavaSyntacticAnalyzer.class.getClassLoader();
        String completePath = loader.getResource(path+"/test_"+fileNumber+".java").getPath();

        try{
            BufferedReader reader = new BufferedReader(new FileReader(completePath));
            reader.readLine();
            String description = reader.readLine() +" -@-> ("+path+" | "+fileNumber+")";
            return Arguments.of(completePath, description);
        } catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

    private final CodeLineFactory lineFactory = new DefaultCodeLineFactory();

    @ParameterizedTest(name = "[{index}] {1}")
    @MethodSource("semanticTestFiles")
    void testFiles(String filePath, String description) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        String failLexeme = bufferedReader.readLine();
        SyntacticAnalyzer testSubject = getSyntacticAnalyzer(bufferedReader);

        SymbolTable.getInstance().reset(); // Clear classes and interfaces of previous tests
        try{
            testSubject.analyze();
            SymbolTable.getInstance().consolidate();
            assertEquals(failLexeme, "", "Test pass, but we expected an exception"); // If does not throw exception then we should expect an exception
        } catch (SemanticException e){
            Token eToken = e.getExceptionToken();
            System.out.println("Exception at ["+eToken.getLineNumber()+", "+eToken.getColumnNumber()+"] token "+e.getExceptionToken().getType()+ " message: "+e.getMessage());
            assertNotEquals("", failLexeme, "We expected the test to pass, but it failed");
            assertEquals(eToken.getLexeme().toString(), failLexeme, "The fail lexeme is not the one we expected");
        } catch (SyntacticException e){
            fail("The analyzer threw a SyntacticException: \n"+e.getMessage());
        }
    }

    private MiniJavaSyntacticAnalyzer getSyntacticAnalyzer(BufferedReader bufferedReader) {
        CodeLinesReader linesReader = new BufferedCodeLinesReader(bufferedReader, lineFactory);
        SourceCodeReader reader = new DefaultSourceCodeReader(linesReader);
        MiniJavaLexicalAnalyzer lexicalAnalyzer = new MiniJavaLexicalAnalyzer(reader);
        LexicalSequence lexicalSequence = new LexicalSequence(lexicalAnalyzer);
        return new MiniJavaSyntacticAnalyzer(lexicalSequence);
    }

}
