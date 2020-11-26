package semantic.ast.sentence;

import lexical.Token;
import semantic.SemanticException;
import semantic.ast.LocalScope;
import semantic.ast.expression.ExpressionNode;
import semantic.ast.LocalVariable;
import semantic.ast.sentence.visitor.SentenceVisitor;
import semantic.symbol.attribute.type.PrimitiveType;

import java.util.List;

public class WhileSentenceNode implements SentenceNode {

    private final Token whileToken;
    private final ExpressionNode condition;
    private final SentenceNode sentence;

    public WhileSentenceNode(Token whileToken, ExpressionNode condition, SentenceNode sentence) {
        this.whileToken = whileToken;
        this.condition = condition;
        this.sentence = sentence;
    }

    @Override
    public void validate(LocalScope scope) {
        condition.validate(scope);
        sentence.validate(scope);

        if (!PrimitiveType.BOOLEAN().equals(condition.getType())){
            throw new SemanticException("La condicion del WHILE debe ser booleana", whileToken);
        }
    }

    @Override
    public void accept(SentenceVisitor visitor) {
        visitor.visit(this);
    }
}
