package semantic.ast.expression;

import semantic.ast.ASTNode;
import semantic.symbol.attribute.type.Type;

public interface ExpressionNode extends ASTNode {

    Type getType();

}
