package semantic.ast.sentence;

import lexical.Token;
import semantic.ast.LocalScope;
import semantic.ast.expression.ExpressionNode;
import semantic.ast.LocalVariable;
import semantic.ast.sentence.visitor.SentenceVisitor;

import java.util.List;

public class IfElseSentenceNode extends IfSentenceNode {

    private final SentenceNode elseSentence;

    public IfElseSentenceNode(Token ifToken, ExpressionNode ifCondition, SentenceNode ifSentence, SentenceNode elseSentence) {
        super(ifToken, ifCondition, ifSentence);
        this.elseSentence = elseSentence;
    }


    @Override
    public void validate(LocalScope scope) {
        elseSentence.validate(scope);
    }

    @Override
    public void accept(SentenceVisitor visitor) {
        visitor.visit(this);
    }

}
