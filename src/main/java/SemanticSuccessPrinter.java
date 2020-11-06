import com.google.common.base.Joiner;
import lexical.Token;
import util.Characters;

import java.io.PrintStream;

public class SemanticSuccessPrinter {

    private final PrintStream out;

    public SemanticSuccessPrinter(PrintStream out) {
        this.out = out;
    }

    public void printSuccess(){
        out.println("Compilación exitosa");
        out.println();
        out.println("[SinErrores]");
    }
}
