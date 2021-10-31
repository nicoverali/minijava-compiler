package semantic.ast.expression;

import semantic.ast.ASTNode;
import semantic.symbol.attribute.type.Type;

public interface ExpressionNode extends ASTNode {

    /**
     * Returns the {@link Type} of this expression. Note this expression may be composed of several other expressions, thus
     * the type may depend on the type of other expressions.
     * The currents scope is needed so that generic cases are handled correctly.
     * <br>
     * To avoid any issue, make sure to validate this expression before calling this method, since checks from
     * validation may be assumed here.
     *
     * @return the {@link Type} of this expression
     */
    Type getType();
}
