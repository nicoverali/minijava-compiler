package semantic.ast.sentence.assignment;

import lexical.Token;
import semantic.ast.access.AccessNode;
import semantic.ast.expression.ExpressionNode;

public class AssignmentSentenceFactory {

    public static AssignmentSentenceNode create(AccessNode leftSide, Token assignmentType, ExpressionNode rightSide){
        if (assignmentType.getLexeme().toString().equals("=")){
            return new AssignmentSentenceNode(leftSide, assignmentType, rightSide);
        } else {
            return new SpecialAssignmentSentenceNode(leftSide, assignmentType, rightSide);
        }
    }

}
