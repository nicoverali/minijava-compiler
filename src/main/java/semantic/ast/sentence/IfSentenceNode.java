package semantic.ast.sentence;

import lexical.Token;
import semantic.SemanticException;
import semantic.ast.scope.Scope;
import semantic.ast.expression.ExpressionNode;
import semantic.ast.sentence.visitor.SentenceVisitor;
import semantic.symbol.attribute.type.PrimitiveType;
import semantic.symbol.attribute.type.Type;

public class IfSentenceNode implements SentenceNode {

    protected final Token ifToken;
    protected final ExpressionNode ifCondition;
    protected final SentenceNode ifSentence;
    protected final SentenceNode elseSentence;

    public IfSentenceNode(Token ifToken, ExpressionNode ifCondition, SentenceNode ifSentence) {
        this(ifToken, ifCondition, ifSentence, null);
    }

    public IfSentenceNode(Token ifToken, ExpressionNode ifCondition, SentenceNode ifSentence, SentenceNode elseSentence) {
        this.ifToken = ifToken;
        this.ifCondition = ifCondition;
        this.ifSentence = ifSentence;
        this.elseSentence = elseSentence;
    }

    @Override
    public void validate(Scope scope) {
        ifCondition.validate(scope);
        ifSentence.validate(scope);

        if (elseSentence != null) elseSentence.validate(scope);

        Type ifConditionType = ifCondition.getType();
        if (!ifConditionType.equals(PrimitiveType.BOOLEAN())){
            throw new SemanticException("La condicion del IF debe ser booleana", ifToken);
        }
    }

    @Override
    public void accept(SentenceVisitor visitor) {
        visitor.visit(this);
    }

}
