package semantic.ast.expression.access;

import asm.ASMWriter;
import lexical.Token;
import semantic.SemanticException;
import semantic.ast.asm.ASMContext;
import semantic.ast.scope.Scope;
import semantic.symbol.SymbolTable;
import semantic.symbol.attribute.type.ReferenceType;
import semantic.symbol.attribute.type.Type;

import static semantic.symbol.finder.AncestorFinder.isAncestor;

public class CastAccessNode extends BaseAccessNode {

    private final ReferenceType castType;
    private final AccessNode access;

    public CastAccessNode(ReferenceType castType, AccessNode access) {
        this.castType = castType;
        this.access = access;
    }

    @Override
    protected void validateAccess(Scope scope) {
        castType.validate(SymbolTable.getInstance());
        access.validate(scope);
        Type accessType = access.getType();

        if (!(accessType instanceof ReferenceType)) throw new SemanticException("Solo se puede castear tipos clase", castType);
        if (!isAncestor((ReferenceType) accessType, castType)) throw new SemanticException("Tipos incompatibles", castType);
    }

    @Override
    public Type getType() {
        return castType;
    }

    @Override
    public Type getAccessType() {
        return castType;
    }

    @Override
    public Token toToken() {
        return access.getChainEnd().toToken();
    }

    @Override
    public void generateAccess(ASMContext context, ASMWriter writer) {
        access.generate(context, writer);
    }
}
