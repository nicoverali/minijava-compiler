package semantic.ast.sentence.assignment;

import lexical.Token;
import semantic.SemanticException;
import semantic.ast.expression.access.AccessNode;
import semantic.ast.expression.access.VarAccessNode;
import semantic.ast.expression.access.chain.ChainedAttrNode;
import semantic.ast.scope.Scope;
import semantic.ast.sentence.SentenceNode;
import semantic.ast.sentence.visitor.SentenceVisitor;

import static semantic.symbol.attribute.type.PrimitiveType.INT;

public class IncrDecrSentenceNode implements AssignmentNode {

    private final AccessNode access;
    private final Token incrementToken;

    public IncrDecrSentenceNode(AccessNode access, Token incrementToken) {
        this.access = access;
        this.incrementToken = incrementToken;
    }

    @Override
    public void validate(Scope scope) {
        access.validate(scope);

        AccessNode lastAccess = access.getChainEnd();
        if (!((lastAccess instanceof VarAccessNode) || (lastAccess instanceof ChainedAttrNode)))
            throw new SemanticException("Solo se puede asignar a una variable o atributo de instancia", access.toToken());
        if (!lastAccess.getType().equals(INT()))
            throw new SemanticException("Tipo no compatible", access.toToken());
    }

    @Override
    public void accept(SentenceVisitor visitor) {

    }
}
