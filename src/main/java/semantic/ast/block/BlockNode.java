package semantic.ast.block;

import lexical.Token;
import semantic.ast.ASTNode;
import semantic.ast.scope.Scope;
import semantic.ast.sentence.SentenceNode;

import java.util.ArrayList;
import java.util.List;

public class BlockNode implements ASTNode {

    private final Token openBracket;
    private final List<SentenceNode> sentences;
    private final Token closeBracket;

    public static BlockNode empty(){
        return new BlockNode(null, new ArrayList<>(), null);
    }

    public BlockNode(Token openBracket, List<SentenceNode> sentences, Token closeBracket) {
        this.openBracket = openBracket;
        this.sentences = sentences;
        this.closeBracket = closeBracket;
    }

    public void add(SentenceNode sentence){
        sentences.add(sentence);
    }

    public List<SentenceNode> getSentences(){
        return sentences;
    }

    public Token getOpenBracket() {
        return openBracket;
    }

    public Token getCloseBracket() {
        return closeBracket;
    }

    @Override
    public void validate(Scope scope) {
        sentences.forEach(sentence -> sentence.validate(scope));
    }
}
