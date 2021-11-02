package semantic.ast.sentence.assignment;

import lexical.Token;
import semantic.SemanticException;
import semantic.ast.expression.access.StaticVarAccessNode;
import semantic.ast.expression.access.chain.ChainedAttrNode;
import semantic.ast.scope.Scope;
import semantic.ast.expression.access.AccessNode;
import semantic.ast.expression.access.VarAccessNode;
import semantic.ast.expression.ExpressionNode;
import semantic.ast.sentence.SentenceNode;
import semantic.ast.sentence.visitor.SentenceVisitor;
import semantic.symbol.attribute.type.ReferenceType;
import semantic.symbol.attribute.type.Type;
import semantic.symbol.finder.AncestorFinder;

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

        Type leftType = leftSide.getType();
        Type rightType = rightSide.getType();

        if (leftType.equals(rightType)) return;

        if (!(leftType instanceof ReferenceType)) throw new SemanticException("El lado derecho de la asignacion no conforma el lado izquierdo", assignmentType);
        if (!(rightType instanceof ReferenceType)) throw new SemanticException("El lado derecho de la asignacion no conforma el lado izquierdo", assignmentType);

        if (!(AncestorFinder.isAncestor((ReferenceType) leftType, (ReferenceType) rightType)))
            throw new SemanticException("El lado derecho de la asignacion no conforma el lado izquierdo", assignmentType);
    }

    private void validateLeftAccess(Scope scope) {
        // This will ensure that all parts of the chain are valid
        leftSide.validate(scope);

        AccessNode lastAccess = leftSide.getChainEnd();
        if (!(lastAccess instanceof VarAccessNode || lastAccess instanceof ChainedAttrNode || lastAccess instanceof StaticVarAccessNode)){
            throw new SemanticException("El acceso debe ser una variable o atributo de instancia", lastAccess.toToken());
        }
    }

    @Override
    public void accept(SentenceVisitor visitor) {
        visitor.visit(this);
    }

}
