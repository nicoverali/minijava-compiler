package grammar.generator;

import com.google.common.base.Joiner;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class GeneratorPrinter {

    private static final String EQUALS_METHOD = "equalsAny";

    private final PrintWriter out;

    public GeneratorPrinter(String filePath) throws IOException {
         out = new PrintWriter(new FileWriter(filePath));
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
        out.printf("\tif (%s(%s)) {\n", EQUALS_METHOD, tokensStr);
        body.getActions().forEach(action -> out.println("\t\t"+action));
        out.print("\t} ");
        printElseBody(method);
    }

    private void printMethodSignature(GeneratorMethod method){
        out.println("private void "+method.getName()+"() {");
    }

    private void printSingleBody(GeneratorMethod method){
        GeneratorMethodBody body = method.getMethodBodies().get(0);
        body.getActions().forEach(action -> out.println("\t"+action));
    }

    private void printMultipleBodies(GeneratorMethod method){
        String IF_START = "\tif ";
        for (GeneratorMethodBody body : method){
            String tokens = Joiner.on(", ").join(body.getPredicateTokens());
            out.printf("%s (%s(%s)) {\n", IF_START, EQUALS_METHOD, tokens);
            body.getActions().forEach(action -> out.println("\t\t"+action));
            out.print("\t} ");
            IF_START = "else if ";
        }
    }

    private void printElseBody(GeneratorMethod method){
        if (!method.hasLambda()){
            out.println("else {");
                out.println("\t\tsequence.next().ifPresent(token -> ");
                out.println("\t\t\t{throw new SyntacticException(\"Se esperaba ("+method.getName()+") pero se encontro \"+token.getType(), token);});");
            out.println("\t}");
        } else {
            out.println("");
        }
    }

    private void printMethodClose(){
        out.println('}');
        out.println();
    }

}
