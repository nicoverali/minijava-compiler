package syntactic;

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
import util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MiniJavaSyntacticAnalyzerTest {

    private static final String BASE = "syntactic_tests/";
    private static final List<Pair<String, Integer>> paths = new ArrayList<>();
    static {
        paths.add(Pair.of(BASE+"base", 33));
        paths.add(Pair.of(BASE+"inline_assign", 4));
        paths.add(Pair.of(BASE+"implicit_attributes", 3));
        paths.add(Pair.of(BASE+"generics", 8));
        paths.add(Pair.of(BASE+"operator_precedence", 1));
        paths.add(Pair.of(BASE+"interfaces", 18));
        paths.add(Pair.of(BASE+"static_attributes", 7));
    }


    private static Stream<Arguments> syntacticTestFiles() {
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

    CodeLineFactory lineFactory = new DefaultCodeLineFactory();

    @ParameterizedTest(name = "[{index}] {1}")
    @MethodSource("syntacticTestFiles")
    void testFiles(String filePath, String description) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        String errorToken = getErrorToken(bufferedReader);
        SyntacticAnalyzer testSubject = getSyntacticAnalyzer(bufferedReader);

        try{
            testSubject.analyze();
            assertEquals("", errorToken, "Test pass, but we expected an exception"); // If does not throw exception then we should expect an exception
        } catch (SyntacticException e){
            Token eToken = e.getExceptionToken();
            System.out.println("Exception at ["+eToken.getLineNumber()+", "+eToken.getColumnNumber()+"] token "+e.getExceptionToken().getType()+ " message: "+e.getMessage());
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