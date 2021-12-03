package semantic.ast.expression.access.chain;

import asm.ASMWriter;
import lexical.Token;
import semantic.SemanticException;
import semantic.ast.asm.ASMContext;
import semantic.ast.expression.access.AccessNode;
import semantic.ast.expression.access.VariableAccess;
import semantic.ast.scope.Scope;
import semantic.symbol.AttributeSymbol;
import semantic.symbol.ClassSymbol;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.Type;
import semantic.symbol.finder.AttributeFinder;

import java.util.Optional;

import static semantic.symbol.attribute.IsPublicAttribute.emptyPublic;

public class ChainedAttrNode extends BaseChainNode implements VariableAccess {

    private final NameAttribute name;

    private AttributeSymbol attrSymbol;
    private Side side = Side.RIGHT;

    public ChainedAttrNode(AccessNode leftAccess, Token dotToken, NameAttribute name) {
        super(leftAccess, dotToken);
        this.name = name;
    }

    public ChainedAttrNode(AccessNode leftAccess, ChainNode chain, Token dotToken, NameAttribute name) {
        super(leftAccess, dotToken, chain);
        this.name = name;
    }

    @Override
    public Type getAccessType() {
        if (attrSymbol == null) throw new IllegalStateException("Make sure to validate before getting the type of an expression");
        return attrSymbol.getType();
    }

    @Override
    public void validateAccess(Scope scope) {
        ClassSymbol classSym = validateLeftAccess();

        // Find this attribute within the left referenced class
        Optional<AttributeSymbol> attr = new AttributeFinder(classSym)
                .find(emptyPublic(), name);
        if (attr.isEmpty()) throw new SemanticException("No se pudo encontrar el atributo", name);
        attrSymbol = attr.get();
    }

    @Override
    public Token toToken() {
        return name.getToken();
    }

    @Override
    public void setSide(Side side) {
        this.side = side;
    }

    @Override
    public void generateAccess(ASMContext context, ASMWriter writer) {
        removePrevReferenceIfStatic(writer);

        if (side.equals(Side.RIGHT)) {
            attrSymbol.generateASMLoad(context, writer);
        } else {
            attrSymbol.generateASMStore(context, writer);
        }
    }

    private void removePrevReferenceIfStatic(ASMWriter writer) {
        if (attrSymbol.isDynamic()) return;

        writer.writeln("FMEM 1\t;\tRemovemos la referencia anterior porque el atributo es estatico");
    }
}
