package semantic.ast.sentence;

import semantic.ast.Scope;
import semantic.ast.block.BlockNode;
import semantic.ast.sentence.visitor.SentenceVisitor;
import semantic.symbol.TopLevelSymbol;

public class BlockSentenceNode implements SentenceNode {

    private final BlockNode block;
    private boolean hasGeneric;

    public BlockSentenceNode(BlockNode block) {
        this.block = block;
    }

    @Override
    public void validate(Scope scope) {
        block.validate(scope);
        hasGeneric = block.hasGenerics(scope);
    }

    @Override
    public void accept(SentenceVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public SentenceNode instantiate(TopLevelSymbol container, String newType) {
        if (hasGeneric){
            return new BlockSentenceNode(block.instantiate(container, newType));
        }
        return this;
    }

    @Override
    public boolean hasGenerics(TopLevelSymbol container) {
        return hasGeneric;
    }
}
