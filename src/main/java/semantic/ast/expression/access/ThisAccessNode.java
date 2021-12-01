package semantic.ast.expression.access;

import asm.ASMWriter;
import lexical.Token;
import semantic.SemanticException;
import semantic.ast.asm.ASMContext;
import semantic.ast.scope.Scope;
import semantic.ast.expression.access.chain.ChainNode;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.ReferenceType;
import semantic.symbol.attribute.type.Type;

public class ThisAccessNode extends BaseAccessNode {

    private final Token thisToken;
    private ReferenceType thisRef;

    public ThisAccessNode(Token thisToken) {
        this.thisToken = thisToken;
    }

    public ThisAccessNode(ChainNode chain, Token thisToken) {
        super(chain);
        this.thisToken = thisToken;
    }

    @Override
    public Type getAccessType() {
        return thisRef;
    }

    @Override
    public void validateAccess(Scope scope) {
        if (scope.isStaticContext()){
            throw new SemanticException("No se puede acceder a this en un contexto estatico", thisToken);
        }
        thisRef = new ReferenceType(scope.getClassContainer().getName());
    }

    @Override
    public Token toToken() {
        return thisToken;
    }

    @Override
    public void generateAccess(ASMContext context, ASMWriter writer) {
        writer.writeln("LOAD 3\t;\tLoad this");
    }
}
