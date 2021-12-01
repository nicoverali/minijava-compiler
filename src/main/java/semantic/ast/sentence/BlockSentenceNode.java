package semantic.ast.sentence;

import asm.ASMWriter;
import lexical.Token;
import semantic.ast.asm.ASMContext;
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

    @Override
    public void generate(ASMContext context, ASMWriter writer) {
        ASMContext subContext = context.createSubContext();
        for (SentenceNode sentence : block.getSentences()) {
            sentence.generate(subContext, writer);
        }

        int newVariables = subContext.numberOfVariables() - context.numberOfVariables();
        writer.writeln("FMEM %s\t;\tLiberamos las variables del subscope", newVariables);
    }
}
