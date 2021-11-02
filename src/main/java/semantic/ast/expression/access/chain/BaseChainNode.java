package semantic.ast.expression.access.chain;

import lexical.Token;
import semantic.SemanticException;
import semantic.ast.expression.access.AccessNode;
import semantic.ast.expression.access.BaseAccessNode;
import semantic.symbol.ClassSymbol;
import semantic.symbol.SymbolTable;
import semantic.symbol.attribute.type.ReferenceType;
import semantic.symbol.attribute.type.Type;

import java.util.Optional;

public abstract class BaseChainNode extends BaseAccessNode implements ChainNode{

    private final SymbolTable ST = SymbolTable.getInstance();

    protected final Token dotToken;
    protected final AccessNode leftAccess;

    public BaseChainNode(AccessNode leftAccess, Token dotToken) {
        this.leftAccess = leftAccess;
        this.dotToken = dotToken;
    }

    public BaseChainNode(AccessNode leftAccess, Token dotToken, ChainNode chain) {
        super(chain);
        this.leftAccess = leftAccess;
        this.dotToken = dotToken;
    }

    /**
     * Makes sure the left access is valid and returns the {@link ClassSymbol} referenced by it
     */
    protected ClassSymbol validateLeftAccess(){
        // Check that the left type is a reference
        Type leftType = leftAccess.getAccessType();
        if (!(leftType instanceof ReferenceType)){
            throw new SemanticException("Solo se puede encadenar un tipo referencia", dotToken);
        }

        // Check that the left type exists
        Optional<ClassSymbol> symbol = ST.getClass((ReferenceType) leftType);
        if (symbol.isEmpty()) {
            throw new SemanticException("La clase no existe", leftType);
        }

        return symbol.get();
    }

}
