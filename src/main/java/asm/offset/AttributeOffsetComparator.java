package asm.offset;

import semantic.symbol.AttributeSymbol;
import semantic.symbol.MethodSymbol;

import java.util.Comparator;

/**
 * Orders {@link AttributeSymbol} according to their offsets. Lower offset attributes will be first.
 */
public class AttributeOffsetComparator implements Comparator<AttributeSymbol> {

    private final ASMOffsetsGenerator offsetsGenerator;

    /**
     * The given {@link ASMOffsetsGenerator} must already have generated offsets.
     *
     * @see ASMOffsetsGenerator#generateOffsets()
     * @param offsetsGenerator the generator from where offsets of attributes will be retrieved
     */
    public AttributeOffsetComparator(ASMOffsetsGenerator offsetsGenerator) {
        this.offsetsGenerator = offsetsGenerator;
    }


    @Override
    public int compare(AttributeSymbol o1, AttributeSymbol o2) {
        return (int) Math.signum(offsetsGenerator.getAttributeOffset(o1) - offsetsGenerator.getAttributeOffset(o2));
    }
}
