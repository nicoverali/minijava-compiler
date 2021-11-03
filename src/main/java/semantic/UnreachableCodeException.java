package semantic;

import io.code.CodeLine;
import lexical.Token;
import semantic.ast.sentence.SentenceNode;

public class UnreachableCodeException extends RuntimeException {

    private final SentenceNode sentenceNode;

    public UnreachableCodeException(String message, SentenceNode sentenceNode) {
        super(message);
        this.sentenceNode = sentenceNode;
    }

    public String getLine(){
        return sentenceNode.toToken().getFirstLine().map(CodeLine::toString).orElse("");
    }

    public Token getExceptionToken(){
        return sentenceNode.toToken();
    }

    public int getLineNumber(){
        return sentenceNode.toToken().getFirstLine().map(CodeLine::getLineNumber).orElse(0);
    }

}
