package semantic.ast.sentence;

import lexical.Token;
import semantic.SemanticException;
import semantic.ast.expression.access.ConstructorAccessNode;
import semantic.ast.expression.access.MethodAccessNode;
import semantic.ast.expression.access.StaticMethodAccessNode;
import semantic.ast.expression.access.chain.ChainedMethodNode;
import semantic.ast.scope.Scope;
import semantic.ast.expression.access.AccessNode;
import semantic.ast.sentence.visitor.SentenceVisitor;

public class CallSentenceNode implements SentenceNode {

    private final AccessNode access;
    private final Token semicolonToken;

    public CallSentenceNode(AccessNode access, Token semicolonToken) {
        this.access = access;
        this.semicolonToken = semicolonToken;
    }

    @Override
    public void validate(Scope scope) {
        access.validate(scope);

        AccessNode lastAccess = access.getChainEnd();

        if (!(lastAccess instanceof MethodAccessNode || lastAccess instanceof ChainedMethodNode || lastAccess instanceof StaticMethodAccessNode || lastAccess instanceof ConstructorAccessNode)){
            throw new SemanticException("Una sentencia de llamada debe tener un acceso a un metodo", semicolonToken);
        }
    }

    @Override
    public void accept(SentenceVisitor visitor) {
        visitor.visit(this);
    }
}
