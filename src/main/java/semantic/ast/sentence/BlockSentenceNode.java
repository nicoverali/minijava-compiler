package semantic.ast.sentence;

import lexical.Token;
import semantic.ast.scope.Scope;
import semantic.ast.block.BlockNode;
import semantic.ast.sentence.visitor.SentenceVisitor;

public class BlockSentenceNode implements SentenceNode {

    private final BlockNode block;

    public BlockSentenceNode(BlockNode block) {
        this.block = block;
    }

    public BlockNode getBlock() {
        return block;
    }

    @Override
    public void validate(Scope scope) {
        Scope subScope = scope.createSubScope();
        block.validate(subScope);
    }

    @Override
    public void accept(SentenceVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Token toToken() {
        return block.getOpenBracket();
    }
}
