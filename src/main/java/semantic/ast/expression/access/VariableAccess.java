package semantic.ast.expression.access;

public interface VariableAccess extends AccessNode {

    enum Side {LEFT, RIGHT}

    /**
     * Sets the side of this variable access. By default, it will be {@link Side#LEFT} side
     *
     * @param side the side of this access
     */
    void setSide(Side side);

}
