import lexical.TokenType;
import lexical.automata.node.builder.TokenizerNodeBuilder;

public class Main {


    public static void main(String[] args) {
        new TokenizerNodeBuilder("This is a name")
                .ifAny().storeInLexeme().thenRepeat()
                .orElseReturnToken(TokenType.EOF);
    }
}
