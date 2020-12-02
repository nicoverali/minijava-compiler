package semantic.ast.sentence.assignment;

import lexical.Token;
import semantic.SemanticException;
import semantic.ast.Scope;
import semantic.ast.access.AccessNode;
import semantic.ast.access.VarAccessNode;
import semantic.ast.expression.ExpressionNode;
import semantic.ast.sentence.SentenceNode;
import semantic.ast.sentence.visitor.SentenceVisitor;
import semantic.symbol.TopLevelSymbol;

public class AssignmentSentenceNode implements SentenceNode {

    protected final AccessNode leftSide;
    protected final Token assignmentType;
    protected final ExpressionNode rightSide;

    private boolean hasGeneric;

    public AssignmentSentenceNode(AccessNode leftSide, Token assignmentType, ExpressionNode rightSide) {
        this.leftSide = leftSide;
        this.assignmentType = assignmentType;
        this.rightSide = rightSide;
    }

    @Override
    public void validate(Scope scope) {
        validateLeftAccess(scope);
        rightSide.validate(scope);
        hasGeneric = leftSide.hasGenerics(scope) || rightSide.hasGenerics(scope);

        if (!leftSide.getType(scope).equals(rightSide.getType(scope))){
            throw new SemanticException("El lado derecho de la asignacion no conforma el lado izquierdo", assignmentType);
        }
    }

    private void validateLeftAccess(Scope scope) {
        if (leftSide.hasChainedAccess()){
            AccessNode chainEnd = leftSide.getChainEnd();
            if (!(chainEnd instanceof VarAccessNode)){
                throw new SemanticException("El acceso debe ser una variable", chainEnd.getName());
            }
        } else if (!scope.findVariable(leftSide.getName()).isPresent()){
            throw new SemanticException("El acceso debe ser una variable declarada", leftSide.getName());
        }
    }

    @Override
    public void accept(SentenceVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public AssignmentSentenceNode instantiate(TopLevelSymbol container, String newType) {
        if (hasGeneric){
            return new AssignmentSentenceNode(leftSide.instantiate(container, newType), assignmentType, rightSide.instantiate(container, newType));
        }
        return this;
    }

    @Override
    public boolean hasGenerics(TopLevelSymbol container) {
        return hasGeneric;
    }
}
