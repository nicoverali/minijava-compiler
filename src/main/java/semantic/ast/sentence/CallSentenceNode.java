package semantic.ast.sentence;

import semantic.SemanticException;
import semantic.ast.expression.access.MethodAccessNode;
import semantic.ast.expression.access.chain.ChainedMethodNode;
import semantic.ast.scope.Scope;
import semantic.ast.expression.access.AccessNode;
import semantic.ast.sentence.visitor.SentenceVisitor;

public class CallSentenceNode implements SentenceNode {

    private final AccessNode access;

    public CallSentenceNode(AccessNode access) {
        this.access = access;
    }

    @Override
    public void validate(Scope scope) {
        access.validate(scope);

        AccessNode lastAccess = access.getChainEnd();

        if (!(lastAccess instanceof MethodAccessNode) && !(lastAccess instanceof ChainedMethodNode)){
            throw new SemanticException("Una sentencia de llamada de tener un acceso a un metodo", lastAccess.getName());
        }
    }

    @Override
    public void accept(SentenceVisitor visitor) {
        visitor.visit(this);
    }
}
