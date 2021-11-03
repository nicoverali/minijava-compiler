package semantic.ast.sentence.visitor;

import semantic.UnreachableCodeException;
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
        if (visitedReturn) throw new UnreachableCodeException("Codigo muerto", sentence);
    }

    @Override
    public void visit(AssignmentSentenceNode sentence) {
        if (visitedReturn) throw new UnreachableCodeException("Codigo muerto", sentence);
    }

    @Override
    public void visit(DeclarationSentenceNode sentence) {
        if (visitedReturn) throw new UnreachableCodeException("Codigo muerto", sentence);
    }

    @Override
    public void visit(CallSentenceNode sentence) {
        if (visitedReturn) throw new UnreachableCodeException("Codigo muerto", sentence);
    }

    @Override
    public void visit(BlockSentenceNode sentence) {
        if (visitedReturn) throw new UnreachableCodeException("Codigo muerto", sentence);
        sentence.getBlock().getSentences().forEach(s -> s.accept(this));
    }

    @Override
    public void visit(IfSentenceNode sentence) {
        if (visitedReturn) throw new UnreachableCodeException("Codigo muerto", sentence);

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
        if (visitedReturn) throw new UnreachableCodeException("Codigo muerto", sentence);
    }

    @Override
    public void visit(ReturnSentenceNode sentence) {
        if (visitedReturn) throw new UnreachableCodeException("Codigo muerto", sentence);
        visitedReturn = true;
    }
}
