package semantic.ast.sentence.assignment;

import asm.ASMWriter;
import lexical.Token;
import semantic.SemanticException;
import semantic.ast.asm.ASMContext;
import semantic.ast.expression.access.AccessNode;
import semantic.ast.expression.access.VarAccessNode;
import semantic.ast.expression.access.VariableAccess;
import semantic.ast.expression.access.chain.ChainedAttrNode;
import semantic.ast.scope.Scope;
import semantic.ast.sentence.visitor.SentenceVisitor;

import static semantic.symbol.attribute.type.PrimitiveType.INT;

public class IncrDecrSentenceNode implements AssignmentNode {

    private final AccessNode access;
    private final Token incrementToken;

    private VariableAccess lastAccess;

    public IncrDecrSentenceNode(AccessNode access, Token incrementToken) {
        this.access = access;
        this.incrementToken = incrementToken;
    }

    @Override
    public void validate(Scope scope) {
        access.validate(scope);

        AccessNode lastAccess = access.getChainEnd();
        if (!((lastAccess instanceof VarAccessNode) || (lastAccess instanceof ChainedAttrNode)))
            throw new SemanticException("Solo se puede asignar a una variable o atributo de instancia", access.toToken());
        if (!lastAccess.getType().equals(INT()))
            throw new SemanticException("Tipo no compatible", incrementToken);

        this.lastAccess = (VariableAccess) lastAccess;
    }

    @Override
    public void accept(SentenceVisitor visitor) {

    }

    @Override
    public Token toToken() {
        return incrementToken;
    }

    @Override
    public void generate(ASMContext context, ASMWriter writer) {
        access.generate(context, writer);
        writer.writeln("PUSH 1");
        generateOperation(context, writer);

        lastAccess.setSide(VariableAccess.Side.LEFT);
        access.generate(context, writer);
    }

    private void generateOperation(ASMContext context, ASMWriter writer) {
        switch (incrementToken.getType()){
            case ASSIGN_INCR: writer.writeln("ADD\t;\tSumamos 1 al valor anterior"); break;
            case ASSIGN_DECR: writer.writeln("SUB\t;\tRestamos 1 al valor anterior"); break;
        }
    }
}
