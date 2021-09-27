package grammar.generator;

import com.google.common.base.Joiner;

import java.io.PrintWriter;
import java.util.List;

public class GeneratorPrinter {

    private static final String EQUALS_METHOD = "equalsAny";
    private static final int BASE_INDENT = 1;

    private final IndentablePrintWriter out;

    public GeneratorPrinter(PrintWriter out) {
         this.out = new IndentablePrintWriter(out, BASE_INDENT);
    }

    public void print(List<GeneratorMethod> methods){
        for (GeneratorMethod method : methods){
            printMethodSignature(method);
            out.indent();
                if (method.hasLambda() && method.hasSingleBody()) {
                    printSingleBodyWithLambda(method);
                } else if (method.hasSingleBody()) {
                    printSingleBody(method);
                } else {
                    printMultipleBodies(method);
                }
                out.unindent();
            printMethodClose();

        }
        out.close();
    }

    private void printSingleBodyWithLambda(GeneratorMethod method) {
        GeneratorMethodBody body = method.getMethodBodies().get(0);
        String tokensStr = Joiner.on(", ").join(body.getPredicateTokens());
        out.printf("if (%s(%s)) {\n", EQUALS_METHOD, tokensStr);
        out.indent();
            body.getActions().forEach(out::println);
            out.unindent();
        printElseBody(method);
    }

    private void printMethodSignature(GeneratorMethod method){
        out.println("private void "+method.getName()+"() {");
    }

    private void printSingleBody(GeneratorMethod method){
        GeneratorMethodBody body = method.getMethodBodies().get(0);
        body.getActions().forEach(out::println);
    }

    private void printMultipleBodies(GeneratorMethod method){
        String IF_START = "if ";
        for (GeneratorMethodBody body : method){
            String tokens = Joiner.on(", ").join(body.getPredicateTokens());
            out.printf("%s (%s(%s)) {\n", IF_START, EQUALS_METHOD, tokens);
            out.indent();
                body.getActions().forEach(out::println);
                out.unindent();
            IF_START = "} else if ";
        }
        printElseBody(method);
    }

    private void printElseBody(GeneratorMethod method){
        if (!method.hasLambda()){
            out.println("} else {");
            out.indent();
                out.println("sequence.next().ifPresent(token -> ");
                out.indent();
                    out.println("{throw new SyntacticException(\"Se esperaba ("+method.getName()+") pero se encontro \"+token.getType(), token);});");
                    out.unindent();
                out.unindent();
        }
        out.println("}");
    }

    private void printMethodClose(){
        out.println("}");
        out.println();
    }

}
