package semantic.ast.sentence.assignment;

import lexical.Token;
import semantic.SemanticException;
import semantic.ast.Scope;
import semantic.ast.access.AccessNode;
import semantic.ast.expression.ExpressionNode;
import semantic.symbol.attribute.type.PrimitiveType;

public class SpecialAssignmentSentenceNode extends AssignmentSentenceNode {

    public SpecialAssignmentSentenceNode(AccessNode leftSide, Token assignmentType, ExpressionNode rightSide) {
        super(leftSide, assignmentType, rightSide);
    }

    @Override
    public void validate(Scope scope) {
        super.validate(scope);
        if (!PrimitiveType.INT().equals(leftSide.getType(scope))){
            throw new SemanticException("El acceso debe ser de tipo INT para utilizar "+assignmentType.getLexeme(), assignmentType);
        } else if (!PrimitiveType.INT().equals(rightSide.getType(scope))){
            throw new SemanticException("Solo se puede asignar valores de tipo INT", assignmentType);
        }
    }
}
