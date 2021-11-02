package semantic.ast.expression.access;

import lexical.Token;
import semantic.SemanticException;
import semantic.ast.scope.Scope;
import semantic.ast.expression.access.chain.ChainNode;
import semantic.symbol.finder.AttributeFinder;
import semantic.symbol.AttributeSymbol;
import semantic.symbol.ClassSymbol;
import semantic.symbol.SymbolTable;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.ReferenceType;
import semantic.symbol.attribute.type.Type;

import java.util.Optional;

import static semantic.symbol.attribute.IsPublicAttribute.emptyPublic;
import static semantic.symbol.attribute.IsStaticAttribute.emptyStatic;

public class StaticVarAccessNode extends BaseAccessNode {

    private final SymbolTable ST = SymbolTable.getInstance();

    private final ReferenceType containerRef;
    private final NameAttribute attrName;

    private AttributeSymbol attrSym;

    public StaticVarAccessNode(ReferenceType containerRef, NameAttribute attrName) {
        this.containerRef = containerRef;
        this.attrName = attrName;
    }

    public StaticVarAccessNode(ChainNode chain, ReferenceType containerRef, NameAttribute attrName) {
        super(chain);
        this.containerRef = containerRef;
        this.attrName = attrName;
    }

    @Override
    public Type getAccessType() {
        if (attrSym == null) throw new IllegalStateException("Make sure to validate before getting the type of an expression");
        return attrSym.getType();
    }

    @Override
    public void validateAccess(Scope scope) {
        // Validate and find class
        containerRef.validate(ST);
        Optional<ClassSymbol> symbol = ST.getClass(containerRef);
        if (symbol.isEmpty()){
            throw new SemanticException("No se pudo encontrar la clase", containerRef);
        }

        // Find a static attribute within the reference class
        Optional<AttributeSymbol> attr = new AttributeFinder(symbol.get()).find(emptyPublic(), emptyStatic(), attrName);
        if(attr.isEmpty()){
            throw new SemanticException("No se pudo encontrar el atributo", attrName);
        }
        attrSym = attr.get();
    }

    @Override
    public Token toToken() {
        return attrName.getToken();
    }

}
