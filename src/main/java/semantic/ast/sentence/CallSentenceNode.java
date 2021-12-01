package semantic.ast.sentence;

import asm.ASMWriter;
import lexical.Token;
import semantic.SemanticException;
import semantic.ast.asm.ASMContext;
import semantic.ast.expression.access.ConstructorAccessNode;
import semantic.ast.expression.access.MethodAccessNode;
import semantic.ast.expression.access.StaticMethodAccessNode;
import semantic.ast.expression.access.chain.ChainedMethodNode;
import semantic.ast.scope.Scope;
import semantic.ast.expression.access.AccessNode;
import semantic.ast.sentence.visitor.SentenceVisitor;
import semantic.symbol.attribute.type.VoidType;

import static semantic.symbol.attribute.type.VoidType.VOID;

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

    @Override
    public Token toToken() {
        return semicolonToken;
    }

    @Override
    public void generate(ASMContext context, ASMWriter writer) {
        access.generate(context, writer);
        AccessNode lastAccess = access.getChainEnd();

        if (!lastAccess.getAccessType().equals(VOID())) {
            writer.writeln("POP\t;\tDescartamos el valor de retorno de la llamada");
        }
    }
}
