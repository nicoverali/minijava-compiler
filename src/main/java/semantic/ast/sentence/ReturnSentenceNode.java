package semantic.ast.sentence;

import asm.ASMWriter;
import lexical.Token;
import semantic.SemanticException;
import semantic.ast.asm.ASMContext;
import semantic.ast.expression.ExpressionNode;
import semantic.ast.scope.Scope;
import semantic.ast.sentence.visitor.SentenceVisitor;
import semantic.symbol.attribute.type.Type;
import semantic.symbol.attribute.type.VoidType;

import java.util.Optional;

import static semantic.symbol.attribute.type.VoidType.VOID;

public class ReturnSentenceNode implements SentenceNode{

    private final Token returnToken;
    private final ExpressionNode returnExpression;

    public ReturnSentenceNode(Token returnToken, ExpressionNode returnExpression) {
        this.returnToken = returnToken;
        this.returnExpression = returnExpression;
    }

    public ReturnSentenceNode(Token returnToken) {
        this.returnToken = returnToken;
        this.returnExpression = null;
    }

    @Override
    public void validate(Scope scope) {
        Optional<Type> expectedReturnType = scope.getExpectedReturnType();

        if (expectedReturnType.isEmpty() && returnExpression != null){
            throw new SemanticException("No se esperaba un valor de retorno", returnToken);
        } else if (expectedReturnType.isPresent() && returnExpression == null){
            throw new SemanticException("Falta el valor de retorno", returnToken);
        } else if (returnExpression != null){
            returnExpression.validate(scope);
            if (!returnExpression.getType().conforms(expectedReturnType.get())){
                throw new SemanticException("Valor de retorno incompatible", returnToken);
            }
        }
    }

    @Override
    public void accept(SentenceVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Token toToken() {
        return returnToken;
    }

    @Override
    public void generate(ASMContext context, ASMWriter writer) {
        generateReturnExpression(context, writer);

        int retOffset = context.getParameters().size();
        if (context.isDynamic()) retOffset++; // Add offset for "this"

        writer.writeln("FMEM %s\t;\tLiberamos el espacio de las variables locales", context.numberOfVariables());
        writer.writeln("STOREFP\t;\tReestablecer FP a RA anterior");
        writer.writeln("RET %s\t;\tSubir la stack n posiciones para volver al RA anterior", retOffset);
    }

    private void generateReturnExpression(ASMContext context, ASMWriter writer) {
        if (returnExpression == null) return;

        int numberOfParameters = context.getParameters().size();
        int returnOffset = 3 + numberOfParameters;
        if (context.isDynamic()) returnOffset++;

        returnExpression.generate(context, writer);
        writer.writeln("STORE %s", returnOffset);
    }
}
