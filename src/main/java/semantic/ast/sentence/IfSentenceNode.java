package semantic.ast.sentence;

import lexical.Token;
import semantic.SemanticException;
import semantic.ast.Scope;
import semantic.ast.expression.ExpressionNode;
import semantic.ast.sentence.visitor.SentenceVisitor;
import semantic.symbol.TopLevelSymbol;
import semantic.symbol.attribute.type.PrimitiveType;

public class IfSentenceNode implements SentenceNode {

    protected final Token ifToken;
    protected final ExpressionNode ifCondition;
    protected final SentenceNode ifSentence;

    private boolean hasGeneric;

    public IfSentenceNode(Token ifToken, ExpressionNode ifCondition, SentenceNode ifSentence) {
        this.ifToken = ifToken;
        this.ifCondition = ifCondition;
        this.ifSentence = ifSentence;
    }

    @Override
    public void validate(Scope scope) {
        ifCondition.validate(scope);
        ifSentence.validate(scope);
        hasGeneric = ifCondition.hasGenerics(scope) || ifSentence.hasGenerics(scope);

        if (!PrimitiveType.BOOLEAN().equals(ifCondition.getType(scope))){
            throw new SemanticException("La condicion del IF debe ser booleana", ifToken);
        }
    }

    @Override
    public IfSentenceNode instantiate(TopLevelSymbol container, String newType) {
        if (hasGeneric){
            return new IfSentenceNode(ifToken, ifCondition.instantiate(container, newType)
                    , ifSentence.instantiate(container, newType));
        }
        return this;
    }

    @Override
    public boolean hasGenerics(TopLevelSymbol container) {
        return hasGeneric;
    }

    @Override
    public void accept(SentenceVisitor visitor) {
        visitor.visit(this);
    }

}
