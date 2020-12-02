package semantic.ast.sentence;

import lexical.Token;
import semantic.SemanticException;
import semantic.ast.Scope;
import semantic.ast.expression.ExpressionNode;
import semantic.ast.sentence.visitor.SentenceVisitor;
import semantic.symbol.TopLevelSymbol;
import semantic.symbol.attribute.type.PrimitiveType;

public class WhileSentenceNode implements SentenceNode {

    private final Token whileToken;
    private final ExpressionNode condition;
    private final SentenceNode sentence;

    private boolean hasGeneric;

    public WhileSentenceNode(Token whileToken, ExpressionNode condition, SentenceNode sentence) {
        this.whileToken = whileToken;
        this.condition = condition;
        this.sentence = sentence;
    }

    @Override
    public void validate(Scope scope) {
        condition.validate(scope);
        sentence.validate(scope);
        hasGeneric = condition.hasGenerics(scope) || sentence.hasGenerics(scope);

        if (!PrimitiveType.BOOLEAN().equals(condition.getType(scope))){
            throw new SemanticException("La condicion del WHILE debe ser booleana", whileToken);
        }
    }

    @Override
    public void accept(SentenceVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public WhileSentenceNode instantiate(TopLevelSymbol container, String newType) {
        if (hasGeneric){
            return new WhileSentenceNode(whileToken, condition.instantiate(container, newType)
                    , sentence.instantiate(container, newType));
        }
        return this;
    }

    @Override
    public boolean hasGenerics(TopLevelSymbol container) {
        return hasGeneric;
    }
}
