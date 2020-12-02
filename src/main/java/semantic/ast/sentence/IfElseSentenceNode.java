package semantic.ast.sentence;

import lexical.Token;
import semantic.ast.Scope;
import semantic.ast.expression.ExpressionNode;
import semantic.ast.sentence.visitor.SentenceVisitor;
import semantic.symbol.TopLevelSymbol;

public class IfElseSentenceNode extends IfSentenceNode {

    private final SentenceNode elseSentence;

    private boolean hasGeneric;

    public IfElseSentenceNode(Token ifToken, ExpressionNode ifCondition, SentenceNode ifSentence, SentenceNode elseSentence) {
        super(ifToken, ifCondition, ifSentence);
        this.elseSentence = elseSentence;
    }


    @Override
    public void validate(Scope scope) {
        elseSentence.validate(scope);
        hasGeneric = elseSentence.hasGenerics(scope);
    }

    @Override
    public IfSentenceNode instantiate(TopLevelSymbol container, String newType) {
        if (super.hasGenerics(container) || this.hasGeneric){
            return new IfElseSentenceNode(ifToken
                    , ifCondition.instantiate(container, newType)
                    , ifSentence.instantiate(container, newType)
                    , elseSentence.instantiate(container, newType));
        }
        return this;
    }

    @Override
    public boolean hasGenerics(TopLevelSymbol container) {
        return super.hasGenerics(container) || hasGeneric;
    }

    @Override
    public void accept(SentenceVisitor visitor) {
        visitor.visit(this);
    }

}
