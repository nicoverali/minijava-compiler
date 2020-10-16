package grammar.generator;

import com.google.common.base.Joiner;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class GeneratorPrinter {

    private final PrintWriter out;
    private final Scanner input = new Scanner(System.in);
    private boolean askExceptions = false;

    public GeneratorPrinter(String filePath) throws IOException {
         out = new PrintWriter(new FileWriter(filePath));
    }

    public void print(List<GeneratorMethod> methods){
        askIfShouldAskForExceptions();
        for (GeneratorMethod method : methods){
            printMethodSignature(method);
            if (method.hasSingleBody()){
                printSingleBody(method);
            } else {
                printMultipleBodies(method);
                printElseBody(method);
            }
            printMethodClose();

        }
        out.close();
    }

    private void askIfShouldAskForExceptions() {
        boolean repeat;
        do {
            try{
                System.out.print("Ask for exceptions ? (Y|N) :");
                askExceptions = input.nextBoolean();
                repeat = false;
            } catch (InputMismatchException e){
                repeat = true;
            }
            input.nextLine();
        } while (repeat);
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
            out.println(IF_START+"(anyEquals("+ tokens +")) {");
            body.getActions().forEach(action -> out.println("\t\t"+action));
            out.print("\t} ");
            IF_START = "else if ";
        }
    }

    private void printElseBody(GeneratorMethod method){
        out.println("else {");
        if (method.hasLambda()){
            out.println("\t\t// Nada");
        } else {
            if (askExceptions){
                System.out.print("Exception when \""+method.getName()+"\" has no match: ");
                String exception = input.nextLine();
                out.println("\t\tthrow new Exception(\""+exception+"\");");
            } else {
                out.println("\t\tthrow new Exception(\"Generic exception\");");
            }
        }
        out.println("\t}");
    }

    private void printMethodClose(){
        out.println('}');
        out.println();
    }

}
