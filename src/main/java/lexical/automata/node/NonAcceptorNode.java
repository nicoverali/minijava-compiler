package lexical.automata.node;

import io.code.CodeCharacter;
import io.code.CodeLine;
import lexical.LexicalException;
import lexical.Token;

public class NonAcceptorNode extends BaseLexicalNode{

    private final String errorMsg;

    public NonAcceptorNode(String name, String errorMessage) {
        super(name);
        this.errorMsg = errorMessage;
    }

    @Override
    public boolean isAcceptor() {
        return false;
    }

    @Override
    protected Token onNoBranchSelected(CodeLine currentLine, CodeCharacter nextChar) {
        if (nextChar != null){
            throw new LexicalException(errorMsg, nextChar.getCodeLine(), "", nextChar.getColumnNumber());
        } else if (currentLine != null){
            throw new LexicalException(errorMsg, currentLine, "", currentLine.getSize());
        } else {
            throw new LexicalException(errorMsg, null, "", 0);
        }
    }
}
