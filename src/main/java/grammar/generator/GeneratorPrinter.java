package grammar.generator;

import com.google.common.base.Joiner;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.IntStream;

public class GeneratorPrinter {

    private static final String EQUALS_METHOD = "equalsAny";
    private static final int BASE_INDENT = 1;

    private final PrintWriter out;

    public GeneratorPrinter(PrintWriter out) {
         this.out = out;
    }

    public void print(List<GeneratorMethod> methods){
        for (GeneratorMethod method : methods){
            printMethodSignature(method);
            if (method.hasLambda() && method.hasSingleBody()) {
                printSingleBodyWithLambda(method);
            } else if (method.hasSingleBody()) {
                printSingleBody(method);
            } else {
                printMultipleBodies(method);
                printElseBody(method);
            }
            printMethodClose();

        }
        out.close();
    }

    private void printSingleBodyWithLambda(GeneratorMethod method) {
        GeneratorMethodBody body = method.getMethodBodies().get(0);
        String tokensStr = Joiner.on(", ").join(body.getPredicateTokens());
        out.printf(indent("if (%s(%s)) {\n", 1), EQUALS_METHOD, tokensStr);
        body.getActions().forEach(action -> out.println(indent(action, 2)));
        out.print(indent("} ", 1));
        printElseBody(method);
    }

    private void printMethodSignature(GeneratorMethod method){
        out.println(indent("private void "+method.getName()+"() {"));
    }

    private void printSingleBody(GeneratorMethod method){
        GeneratorMethodBody body = method.getMethodBodies().get(0);
        body.getActions().forEach(action -> out.println(indent(action, 1)));
    }

    private void printMultipleBodies(GeneratorMethod method){
        String IF_START = indent("if ", 1);
        for (GeneratorMethodBody body : method){
            String tokens = Joiner.on(", ").join(body.getPredicateTokens());
            out.printf("%s (%s(%s)) {\n", IF_START, EQUALS_METHOD, tokens);
            body.getActions().forEach(action -> out.println(indent(action, 2)));
            out.print(indent("} ", 1));
            IF_START = "else if ";
        }
    }

    private void printElseBody(GeneratorMethod method){
        if (!method.hasLambda()){
            out.println("else {");
                out.println(indent("sequence.next().ifPresent(token -> ", 2));
                out.println(indent("{throw new SyntacticException(\"Se esperaba ("+method.getName()+") pero se encontro \"+token.getType(), token);});", 3));
            out.println(indent("}", 1));
        } else {
            out.println("");
        }
    }

    private void printMethodClose(){
        out.println(indent("}"));
        out.println();
    }

    private String indent(String text){
        return indent(text, 0);
    }

    private String indent(String text, int level) {
        StringBuilder indented = new StringBuilder();
        IntStream.range(0, BASE_INDENT+level).forEach(v -> indented.append("\t"));
        return indented.append(text).toString();
    }

}
