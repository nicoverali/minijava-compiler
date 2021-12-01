package semantic.ast.sentence;

import asm.ASMLabeler;
import asm.ASMWriter;
import lexical.Token;
import semantic.SemanticException;
import semantic.ast.asm.ASMContext;
import semantic.ast.expression.ExpressionNode;
import semantic.ast.scope.Scope;
import semantic.ast.sentence.assignment.AssignmentNode;
import semantic.ast.sentence.visitor.SentenceVisitor;

import static asm.ASMLabeler.label;
import static semantic.symbol.attribute.type.PrimitiveType.BOOLEAN;

public class ForSentenceNode implements SentenceNode {

    private final Token forToken;
    private final DeclarationSentenceNode localVarDeclaration;
    private final ExpressionNode expressionNode;
    private final AssignmentNode assignment;
    private final SentenceNode loopSentence;

    public ForSentenceNode(Token forToken, DeclarationSentenceNode localVarDeclaration, ExpressionNode expressionNode, AssignmentNode assignment, SentenceNode loopSentence) {
        this.forToken = forToken;
        this.localVarDeclaration = localVarDeclaration;
        this.expressionNode = expressionNode;
        this.assignment = assignment;
        this.loopSentence = loopSentence;
    }

    public SentenceNode getLoopSentence() {
        return loopSentence;
    }

    @Override
    public void validate(Scope scope) {
        Scope forScope = scope.createSubScope();
        localVarDeclaration.validate(forScope);
        expressionNode.validate(forScope);
        assignment.validate(forScope);
        loopSentence.validate(forScope);

        if (!expressionNode.getType().equals(BOOLEAN())) throw new SemanticException("La expresion debe ser booleana", forToken);
    }

    @Override
    public void accept(SentenceVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Token toToken() {
        return forToken;
    }

    @Override
    public void generate(ASMContext context, ASMWriter writer) {
        ASMContext forContext = context.createSubContext();
        localVarDeclaration.generate(forContext, writer);

        String loopLabel = label("for_loop");
        String endForLabel = label("end_for");

        writer.writelnLabeled(loopLabel);
        expressionNode.generate(forContext, writer);
        writer.writeln("BF %s\t;\tSi es falso ir al final del for", endForLabel);
        loopSentence.generate(forContext, writer);
        assignment.generate(forContext, writer);
        writer.writeln("JUMP %s\t;\tSaltar de nuevo al loop", loopLabel);

        writer.writelnLabeled(endForLabel);

        writer.writeln("FMEM 1\t;\tLiberamos la variable del for");
    }
}
