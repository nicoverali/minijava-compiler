package semantic.symbol;

public interface ASMCallable {

    /**
     * Returns the label that this callable will use to be called.
     *
     * @return the label of this callable
     */
    String getASMLabel();

}
