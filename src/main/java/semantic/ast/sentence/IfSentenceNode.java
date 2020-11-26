package semantic.ast.sentence;

import lexical.Token;
import semantic.SemanticException;
import semantic.ast.LocalScope;
import semantic.ast.expression.ExpressionNode;
import semantic.ast.LocalVariable;
import semantic.ast.sentence.visitor.SentenceVisitor;
import semantic.symbol.attribute.type.PrimitiveType;

import java.util.List;

public class IfSentenceNode implements SentenceNode {

    private final Token ifToken;
    private final ExpressionNode ifCondition;
    private final SentenceNode ifSentence;

    public IfSentenceNode(Token ifToken, ExpressionNode ifCondition, SentenceNode ifSentence) {
        this.ifToken = ifToken;
        this.ifCondition = ifCondition;
        this.ifSentence = ifSentence;
    }

    @Override
    public void validate(LocalScope scope) {
        ifCondition.validate(scope);
        ifSentence.validate(scope);

        if (!PrimitiveType.BOOLEAN().equals(ifCondition.getType())){
            throw new SemanticException("La condicion del IF debe ser booleana", ifToken);
        }
    }

    @Override
    public void accept(SentenceVisitor visitor) {
        visitor.visit(this);
    }

}
