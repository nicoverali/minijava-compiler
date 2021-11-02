package semantic.ast.expression.access.chain;

import lexical.Token;
import semantic.SemanticException;
import semantic.Variable;
import semantic.ast.expression.access.AccessNode;
import semantic.ast.scope.Scope;
import semantic.symbol.finder.AttributeFinder;
import semantic.symbol.AttributeSymbol;
import semantic.symbol.ClassSymbol;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.Type;

import java.util.Optional;

import static semantic.symbol.attribute.IsPublicAttribute.emptyPublic;
import static semantic.symbol.attribute.IsStaticAttribute.emptyDynamic;

public class ChainedAttrNode extends BaseChainNode{

    private final NameAttribute name;

    private AttributeSymbol attrSymbol;

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
                .find(emptyPublic(), emptyDynamic(), name);
        if (attr.isEmpty()) throw new SemanticException("No se pudo encontrar el atributo", name);
        attrSymbol = attr.get();
    }

    @Override
    public Token toToken() {
        return name.getToken();
    }

}
