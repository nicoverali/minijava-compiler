package semantic.ast.sentence.assignment;

import lexical.Token;
import semantic.SemanticException;
import semantic.ast.expression.access.StaticVarAccessNode;
import semantic.ast.expression.access.chain.ChainedAttrNode;
import semantic.ast.scope.Scope;
import semantic.ast.expression.access.AccessNode;
import semantic.ast.expression.access.VarAccessNode;
import semantic.ast.expression.ExpressionNode;
import semantic.ast.sentence.visitor.SentenceVisitor;

public class AssignmentSentenceNode implements AssignmentNode {

    protected final AccessNode leftSide;
    protected final Token assignmentType;
    protected final ExpressionNode rightSide;

    public AssignmentSentenceNode(AccessNode leftSide, Token assignmentType, ExpressionNode rightSide) {
        this.leftSide = leftSide;
        this.assignmentType = assignmentType;
        this.rightSide = rightSide;
    }

    @Override
    public void validate(Scope scope) {
        validateLeftAccess(scope);
        rightSide.validate(scope);

        if (!rightSide.getType().conforms(leftSide.getType())){
            throw new SemanticException("El lado derecho de la asignacion no conforma el lado izquierdo", assignmentType);
        }
    }

    private void validateLeftAccess(Scope scope) {
        // This will ensure that all parts of the chain are valid
        leftSide.validate(scope);

        AccessNode lastAccess = leftSide.getChainEnd();
        if (!(lastAccess instanceof VarAccessNode || lastAccess instanceof ChainedAttrNode || lastAccess instanceof StaticVarAccessNode)){
            throw new SemanticException("El acceso debe ser una variable o atributo de instancia", assignmentType);
        }
    }

    @Override
    public void accept(SentenceVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Token toToken() {
        return leftSide.toToken();
    }

}
