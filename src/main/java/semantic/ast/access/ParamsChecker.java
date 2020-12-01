package semantic.ast.access;

import semantic.SemanticException;
import semantic.ast.Scope;
import semantic.ast.expression.ExpressionNode;
import semantic.symbol.ParameterSymbol;
import semantic.symbol.attribute.SymbolAttribute;

import java.util.List;

public class ParamsChecker {

    public static void validateParams(List<ExpressionNode> ours, List<ParameterSymbol> theirs, Scope scope, SymbolAttribute<?> error) throws SemanticException{
        if (ours.size() != theirs.size()) throw new SemanticException("Cantidad de parametros incorrecta", error);
        for (int i = 0; i < ours.size(); i++) {
            if (!theirs.get(i).getType().equals(ours.get(i).getType(scope))){
                throw new SemanticException("Los tipos de los parametros no conforman", error);
            }
        }
    }

}
