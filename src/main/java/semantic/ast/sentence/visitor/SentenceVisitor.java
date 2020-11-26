package semantic.ast.sentence.visitor;

import semantic.ast.sentence.*;

public interface SentenceVisitor {

    void visit(EmptySentenceNode sentence);

    void visit(AssignmentSentenceNode sentence);

    void visit(DeclarationSentenceNode sentence);

    void visit(CallSentenceNode sentence);

    void visit(BlockSentenceNode sentence);

    void visit(IfSentenceNode sentence);

    void visit(IfElseSentenceNode sentence);

    void visit(WhileSentenceNode sentence);

}
