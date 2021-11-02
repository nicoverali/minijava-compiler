package semantic.ast.block;

import semantic.ast.ASTNode;
import semantic.ast.scope.Scope;
import semantic.ast.sentence.SentenceNode;

import java.util.ArrayList;
import java.util.List;

public class BlockNode implements ASTNode {

    private final List<SentenceNode> sentences = new ArrayList<>();

    public void add(SentenceNode sentence){
        sentences.add(sentence);
    }

    @Override
    public void validate(Scope scope) {
        sentences.forEach(sentence -> sentence.validate(scope));
    }
}