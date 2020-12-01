package semantic.ast.access;

import lexical.Token;
import semantic.SemanticException;
import semantic.ast.Scope;
import semantic.symbol.ClassSymbol;
import semantic.symbol.SymbolTable;
import semantic.symbol.TopLevelSymbol;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.ReferenceType;
import semantic.symbol.attribute.type.Type;

import java.util.Optional;

public class ChainedVarNode extends BaseChainNode{

    private final SymbolTable ST = SymbolTable.getInstance();
    private final NameAttribute name;

    public ChainedVarNode(Token dotToken, NameAttribute name) {
        super(dotToken);
        this.name = name;
    }

    public ChainedVarNode(ChainNode chain, Token dotToken, NameAttribute name) {
        super(chain, dotToken);
        this.name = name;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent") // We'll assume that this has been already validated
    @Override
    protected Type getTypeFrom(ReferenceType leftChainType, Scope scope) {
        ClassSymbol symbol = ST.getClass(leftChainType).get();
        return symbol.getAttribute(true, false, name).get().getType();
    }

    @Override
    public void validate(ReferenceType leftType, Scope scope) {
        Optional<ClassSymbol> symbol = ST.getClass(leftType);
        if (!symbol.isPresent()) {
            throw new SemanticException("Una interfaz no tiene atributos", name);
        }
        if (!symbol.get().getAttribute(true, false, name).isPresent()){
            throw new SemanticException("No se pudo encontrar el atributo", name);
        }
    }

    @Override
    public ChainNode instantiate(TopLevelSymbol container, String newType) {
        if (hasGenerics()){
            return new ChainedVarNode(chain.instantiate(container, newType), dotToken, name);
        }
        return this;
    }
}
