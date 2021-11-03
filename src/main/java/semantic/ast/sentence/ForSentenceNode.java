package semantic.ast.sentence;

import lexical.Token;
import semantic.SemanticException;
import semantic.ast.expression.ExpressionNode;
import semantic.ast.scope.Scope;
import semantic.ast.sentence.assignment.AssignmentNode;
import semantic.ast.sentence.visitor.SentenceVisitor;

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

    public SentenceNode getLoopSentence() {
        return loopSentence;
    }

    @Override
    public void validate(Scope scope) {
        Scope forScope = scope.createSubScope();
        localVarDeclaration.validate(forScope);
        expressionNode.validate(forScope);
        assignment.validate(forScope);
        loopSentence.validate(forScope);

        if (!expressionNode.getType().equals(BOOLEAN())) throw new SemanticException("La expresion debe ser booleana", forToken);
    }

    @Override
    public void accept(SentenceVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Token toToken() {
        return forToken;
    }
}
