import com.google.common.base.Joiner;
import lexical.Token;
import util.Characters;

import java.io.PrintStream;

public class TokenPrinter {

    private final PrintStream out;

    public TokenPrinter(PrintStream out) {
        this.out = out;
    }

    public void print(Token token){
        int line = token.getLineNumber()+1;
        String lexeme = Characters.explicitSpecialChars(token.getLexeme().toString());
        String tokenData = Joiner.on(',').join(token.getType(), lexeme, line);
        out.println("("+ tokenData +")");
    }
}
