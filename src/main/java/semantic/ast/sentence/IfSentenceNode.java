package semantic.ast.sentence;

import asm.ASMLabeler;
import asm.ASMWriter;
import lexical.Token;
import semantic.SemanticException;
import semantic.ast.asm.ASMContext;
import semantic.ast.scope.Scope;
import semantic.ast.expression.ExpressionNode;
import semantic.ast.sentence.visitor.SentenceVisitor;
import semantic.symbol.attribute.type.PrimitiveType;
import semantic.symbol.attribute.type.Type;

import java.util.Optional;

import static asm.ASMLabeler.label;

public class IfSentenceNode implements SentenceNode {

    protected final Token ifToken;
    protected final ExpressionNode ifCondition;
    protected final SentenceNode ifSentence;
    protected final SentenceNode elseSentence;

    public IfSentenceNode(Token ifToken, ExpressionNode ifCondition, SentenceNode ifSentence) {
        this(ifToken, ifCondition, ifSentence, null);
    }

    public IfSentenceNode(Token ifToken, ExpressionNode ifCondition, SentenceNode ifSentence, SentenceNode elseSentence) {
        this.ifToken = ifToken;
        this.ifCondition = ifCondition;
        this.ifSentence = ifSentence;
        this.elseSentence = elseSentence;
    }

    public SentenceNode getIfSentence() {
        return ifSentence;
    }

    public Optional<SentenceNode> getElseSentence() {
        return Optional.ofNullable(elseSentence);
    }

    @Override
    public void validate(Scope scope) {
        Scope ifSentenceScope = scope.createSubScope();
        ifCondition.validate(scope);
        ifSentence.validate(ifSentenceScope);

        if (elseSentence != null) {
            Scope elseSentenceScope = scope.createSubScope();
            elseSentence.validate(elseSentenceScope);
        }

        Type ifConditionType = ifCondition.getType();
        if (!ifConditionType.equals(PrimitiveType.BOOLEAN())){
            throw new SemanticException("La condicion del IF debe ser booleana", ifToken);
        }
    }

    @Override
    public void accept(SentenceVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Token toToken() {
        return ifToken;
    }

    @Override
    public void generate(ASMContext context, ASMWriter writer) {
        if (elseSentence == null){
            generateSingleIf(context, writer);
        } else {
            generateIfElse(context, writer);
        }
    }

    private void generateSingleIf(ASMContext context, ASMWriter writer) {
        ifCondition.generate(context, writer);

        String endIfLabel = label("end_if");
        writer.writeln("BF %s\t;\tSaltar al final del if", endIfLabel);

        ifSentence.generate(context, writer);

        writer.writelnLabeled(endIfLabel);
    }

    private void generateIfElse(ASMContext context, ASMWriter writer) {
        ifCondition.generate(context, writer);

        String elseLabel = label("else");
        writer.writeln("BF %s\t;\tSaltar a else", elseLabel);

        ifSentence.generate(context, writer);

        writer.writelnLabeled(elseLabel);
        elseSentence.generate(context, writer);
        writer.writelnLabeled(label("end_if"));
    }
}
