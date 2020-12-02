package semantic.ast.access;

import semantic.SemanticException;
import semantic.ast.Scope;
import semantic.ast.access.chain.ChainNode;
import semantic.symbol.ClassSymbol;
import semantic.symbol.SymbolTable;
import semantic.symbol.TopLevelSymbol;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.ReferenceType;
import semantic.symbol.attribute.type.Type;

import java.util.Optional;

public class StaticVarAccessNode extends BaseAccessNode {

    private final SymbolTable ST = SymbolTable.getInstance();

    private final ReferenceType containerRef;
    private final NameAttribute attrName;

    public StaticVarAccessNode(ReferenceType containerRef, NameAttribute attrName) {
        this.containerRef = containerRef;
        this.attrName = attrName;
    }

    public StaticVarAccessNode(ChainNode chain, ReferenceType containerRef, NameAttribute attrName) {
        super(chain);
        this.containerRef = containerRef;
        this.attrName = attrName;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent") // We'll assume that this has already been validated
    @Override
    public Type getType(Scope scope) {
        Type thisType = ST.getClass(containerRef).get()
                .getAttributes().find(true, true, attrName).get()
                .getType();

        if (hasChainedAccess()){
            scope.setLeftChainType(thisType);
            return chain.getType(scope);
        }
        return thisType;
    }

    @Override
    public void validate(Scope scope) {
        Optional<ClassSymbol> symbol = ST.getClass(containerRef);
        if (!symbol.isPresent()){
            throw new SemanticException("Una interfaz no tiene atributos", attrName);
        }
        if(!symbol.get().getAttributes().find(true, true, attrName).isPresent()){
            throw new SemanticException("No se pudo encontrar el atributo", attrName);
        }
    }

    @Override
    public NameAttribute getName() {
        return attrName;
    }

    @Override
    public AccessNode instantiate(TopLevelSymbol container, String newType) {
        if (hasGenerics(container)){
            new StaticVarAccessNode(chain.instantiate(container, newType), containerRef, attrName);
        }
        return this;
    }

}
