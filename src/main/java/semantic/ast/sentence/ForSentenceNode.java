package semantic.ast.sentence;

import lexical.Token;
import semantic.SemanticException;
import semantic.ast.scope.Scope;
import semantic.ast.expression.ExpressionNode;
import semantic.ast.sentence.assignment.AssignmentNode;
import semantic.ast.sentence.assignment.AssignmentSentenceNode;
import semantic.ast.sentence.visitor.SentenceVisitor;
import semantic.symbol.attribute.type.PrimitiveType;

import static semantic.symbol.attribute.type.PrimitiveType.BOOLEAN;

public class ForSentenceNode implements SentenceNode {

    private final Token forToken;
    private final DeclarationSentenceNode localVarDeclaration;
    private final ExpressionNode expressionNode;
    private final AssignmentNode assignment;
    private final SentenceNode loopSentence;

    public ForSentenceNode(Token forToken, DeclarationSentenceNode localVarDeclaration, ExpressionNode expressionNode, AssignmentNode assignment, SentenceNode loopSentence) {
        this.forToken = forToken;
        this.localVarDeclaration = localVarDeclaration;
        this.expressionNode = expressionNode;
        this.assignment = assignment;
        this.loopSentence = loopSentence;
    }

    @Override
    public void validate(Scope scope) {
        localVarDeclaration.validate(scope);
        expressionNode.validate(scope);
        assignment.validate(scope);
        loopSentence.validate(scope);

        if (!expressionNode.getType().equals(BOOLEAN())) throw new SemanticException("La expresion debe ser booleana", forToken);
    }

    @Override
    public void accept(SentenceVisitor visitor) {
        visitor.visit(this);
    }
}
