package semantic.ast.sentence;

import lexical.Token;
import semantic.SemanticException;
import semantic.ast.LocalScope;
import semantic.ast.access.AccessNode;
import semantic.ast.expression.ExpressionNode;
import semantic.ast.sentence.visitor.SentenceVisitor;

public class AssignmentSentenceNode implements SentenceNode {

    private final AccessNode leftSide;
    private final Token assignmentType;
    private final ExpressionNode rightSide;

    public AssignmentSentenceNode(AccessNode leftSide, Token assignmentType, ExpressionNode rightSide) {
        this.leftSide = leftSide;
        this.assignmentType = assignmentType;
        this.rightSide = rightSide;
    }

    @Override
    public void validate(LocalScope scope) {
        leftSide.validate(scope);
        rightSide.validate(scope);

        if (leftSide.getType().equals(rightSide.getType())){
            throw new SemanticException("El lado derecho de la asignacion no conforma el lado izquierdo", assignmentType);
        }
    }

    @Override
    public void accept(SentenceVisitor visitor) {
        visitor.visit(this);
    }
}
