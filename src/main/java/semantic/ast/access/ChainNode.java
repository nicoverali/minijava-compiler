package semantic.ast.access;

import semantic.symbol.TopLevelSymbol;

public interface ChainNode extends AccessNode {

    @Override
    ChainNode instantiate(TopLevelSymbol container, String newType);

}
