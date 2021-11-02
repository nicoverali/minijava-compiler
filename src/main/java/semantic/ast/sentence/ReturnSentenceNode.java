package semantic.ast.sentence;

import lexical.Token;
import semantic.SemanticException;
import semantic.ast.expression.ExpressionNode;
import semantic.ast.scope.Scope;
import semantic.ast.sentence.visitor.SentenceVisitor;
import semantic.symbol.attribute.type.Type;

import java.util.Optional;

public class ReturnSentenceNode implements SentenceNode{

    private final Token returnToken;
    private final ExpressionNode returnExpression;

    public ReturnSentenceNode(Token returnToken, ExpressionNode returnExpression) {
        this.returnToken = returnToken;
        this.returnExpression = returnExpression;
    }

    public ReturnSentenceNode(Token returnToken) {
        this.returnToken = returnToken;
        this.returnExpression = null;
    }

    @Override
    public void validate(Scope scope) {
        Optional<Type> expectedReturnType = scope.getExpectedReturnType();

        if (expectedReturnType.isEmpty() && returnExpression != null){
            throw new SemanticException("No se esperaba un valor de retorno", returnToken);
        } else if (expectedReturnType.isPresent() && returnExpression == null){
            throw new SemanticException("Falta el valor de retorno", returnToken);
        } else if (returnExpression != null){
            returnExpression.validate(scope);
            if (!returnExpression.getType().conforms(expectedReturnType.get())){
                throw new SemanticException("Valor de retorno incompatible", returnToken);
            }
        }
    }

    @Override
    public void accept(SentenceVisitor visitor) {
        visitor.visit(this);
    }
}
