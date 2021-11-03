package semantic.ast.sentence.visitor;

import semantic.SemanticException;
import semantic.ast.sentence.*;
import semantic.ast.sentence.assignment.AssignmentSentenceNode;

import java.util.List;

public class CodeFlowValidator implements SentenceVisitor{

    boolean visitedReturn;

    public void checkUnreachableCode(List<SentenceNode> sentences){
        visitedReturn = false;
        sentences.forEach(sentence -> sentence.accept(this));
    }

    public boolean doesReturnAlways(List<SentenceNode> sentences){
        visitedReturn = false;
        sentences.forEach(sentence -> sentence.accept(this));

        return visitedReturn;
    }

    @Override
    public void visit(EmptySentenceNode sentence) {
        if (visitedReturn) throw new SemanticException("Codigo muerto", sentence.toToken());
    }

    @Override
    public void visit(AssignmentSentenceNode sentence) {
        if (visitedReturn) throw new SemanticException("Codigo muerto", sentence.toToken());
    }

    @Override
    public void visit(DeclarationSentenceNode sentence) {
        if (visitedReturn) throw new SemanticException("Codigo muerto", sentence.toToken());
    }

    @Override
    public void visit(CallSentenceNode sentence) {
        if (visitedReturn) throw new SemanticException("Codigo muerto", sentence.toToken());
    }

    @Override
    public void visit(BlockSentenceNode sentence) {
        if (visitedReturn) throw new SemanticException("Codigo muerto", sentence.toToken());
        sentence.getBlock().getSentences().forEach(s -> s.accept(this));
    }

    @Override
    public void visit(IfSentenceNode sentence) {
        if (visitedReturn) throw new SemanticException("Codigo muerto", sentence.toToken());

        if (sentence.getElseSentence().isEmpty()){
            new CodeFlowValidator().checkUnreachableCode(List.of(sentence.getIfSentence()));
            return;
        }

        if (new CodeFlowValidator().doesReturnAlways(List.of(sentence.getIfSentence()))
                && new CodeFlowValidator().doesReturnAlways(List.of(sentence.getElseSentence().get()))){
            visitedReturn = true;
        }
    }

    @Override
    public void visit(ForSentenceNode sentence) {
        if (visitedReturn) throw new SemanticException("Codigo muerto", sentence.toToken());
    }

    @Override
    public void visit(ReturnSentenceNode sentence) {
        if (visitedReturn) throw new SemanticException("Codigo muerto", sentence.toToken());
        visitedReturn = true;
    }
}
