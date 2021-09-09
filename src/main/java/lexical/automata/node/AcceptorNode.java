package lexical.automata.node;

import io.code.CodeCharacter;
import io.code.CodeLine;
import lexical.Token;
import lexical.TokenType;
import lexical.automata.AutomataLexeme;
import lexical.automata.AutomataToken;

public class AcceptorNode extends BaseLexicalNode{

    private TokenType returnType = null;

    public AcceptorNode(String name) {
        super(name);
    }

    public AcceptorNode(String name, TokenType returnType) {
        super(name);
        this.returnType = returnType;
    }

    @Override
    public boolean isAcceptor() {
        return true;
    }

    @Override
    protected Token onNoBranchSelected(CodeLine currentLine, CodeCharacter nextChar) {
        if (returnType == null) {
            return null;
        }
        else if (currentLine != null){
            return new AutomataToken(returnType ,AutomataLexeme.empty(currentLine));
        }
        return new AutomataToken(returnType, AutomataLexeme.empty());
    }
}
