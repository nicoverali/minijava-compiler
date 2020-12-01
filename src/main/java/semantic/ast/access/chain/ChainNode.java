package semantic.ast.access.chain;

import semantic.ast.access.AccessNode;
import semantic.symbol.TopLevelSymbol;

public interface ChainNode extends AccessNode {

    @Override
    ChainNode instantiate(TopLevelSymbol container, String newType);

}
