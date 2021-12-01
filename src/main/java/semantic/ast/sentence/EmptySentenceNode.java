package semantic.ast.sentence;

import asm.ASMWriter;
import lexical.Token;
import semantic.ast.asm.ASMContext;
import semantic.ast.scope.Scope;
import semantic.ast.sentence.visitor.SentenceVisitor;

public class EmptySentenceNode implements SentenceNode {

    private final Token semicolonToken;

    public EmptySentenceNode(Token semicolonToken) {
        this.semicolonToken = semicolonToken;
    }

    @Override
    public void validate(Scope scope) {
        // Empty sentences are always valid
    }

    @Override
    public void accept(SentenceVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Token toToken() {
        return semicolonToken;
    }

    @Override
    public void generate(ASMContext context, ASMWriter writer) {
        // Empty sentences don't generate any ASM code
    }
}
