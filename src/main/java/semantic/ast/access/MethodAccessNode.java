package semantic.ast.access;

import semantic.ast.LocalScope;
import semantic.symbol.attribute.type.Type;

public class MethodAccessNode implements AccessNode {
    @Override
    public AccessNode getLastAccess() {
        return null; // TODO
    }

    @Override
    public Type getType() {
        return null; // TODO
    }

    @Override
    public void validate(LocalScope scope) {
        // TODO
    }
}
