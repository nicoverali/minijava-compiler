package asm.offset;

import semantic.symbol.MethodSymbol;

import java.util.Comparator;

/**
 * Orders {@link MethodSymbol} according to their offsets. Lower offset methods will be first.
 */
public class MethodOffsetComparator implements Comparator<MethodSymbol> {

    private final ASMOffsetsGenerator offsetsGenerator;

    /**
     * The given {@link ASMOffsetsGenerator} must already have generated offsets.
     *
     * @see ASMOffsetsGenerator#generateOffsets()
     * @param offsetsGenerator the generator from where offsets of methods will be retrieved
     */
    public MethodOffsetComparator(ASMOffsetsGenerator offsetsGenerator) {
        this.offsetsGenerator = offsetsGenerator;
    }

    @Override
    public int compare(MethodSymbol o1, MethodSymbol o2) {
        return (int) Math.signum(offsetsGenerator.getMethodOffset(o1) - offsetsGenerator.getMethodOffset(o2));
    }

}
