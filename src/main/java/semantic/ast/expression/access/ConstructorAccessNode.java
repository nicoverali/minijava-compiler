package semantic.ast.expression.access;

import asm.ASMWriter;
import lexical.Token;
import semantic.SemanticException;
import semantic.ast.asm.ASMContext;
import semantic.ast.expression.ExpressionNode;
import semantic.ast.expression.access.chain.ChainNode;
import semantic.ast.scope.Scope;
import semantic.symbol.ClassSymbol;
import semantic.symbol.ConstructorSymbol;
import semantic.symbol.SymbolTable;
import semantic.symbol.attribute.type.ReferenceType;
import semantic.symbol.attribute.type.Type;
import semantic.symbol.finder.ConstructorFinder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static asm.ASMLabeler.label;
import static asm.ASMLabeler.labelVT;

public class ConstructorAccessNode extends BaseAccessNode{

    private final SymbolTable ST = SymbolTable.getInstance();

    private final ReferenceType classRef;
    private final List<ExpressionNode> paramsExpressions;

    private ConstructorSymbol constructor;
    private ClassSymbol clazz;

    public ConstructorAccessNode(ReferenceType classRef, List<ExpressionNode> paramsExpressions) {
        this.classRef = classRef;
        this.paramsExpressions = paramsExpressions;
    }

    public ConstructorAccessNode(ChainNode chain, ReferenceType classRef, List<ExpressionNode> paramsExpressions) {
        super(chain);
        this.classRef = classRef;
        this.paramsExpressions = paramsExpressions;
    }

    @Override
    public Type getAccessType() {
        return classRef;
    }

    @Override
    public void validateAccess(Scope scope) {
        // Check that class reference is valid and exists
        classRef.validate(ST);
        Optional<ClassSymbol> clazz = ST.getClass(classRef);
        if (clazz.isEmpty()){
            throw new SemanticException("No se pudo encontrar la clase", classRef);
        }

        // Validate expressions and get types of them
        paramsExpressions.forEach(exp -> exp.validate(scope));
        List<Type> params = paramsExpressions.stream().map(ExpressionNode::getType).collect(Collectors.toList());

        // Find constructor with the given parameters types
        Optional<ConstructorSymbol> constructor = new ConstructorFinder(clazz.get()).find(params);
        if (constructor.isEmpty()) throw new SemanticException("No se pudo encontrar un constructor con estos parametros", classRef);

        this.constructor = constructor.get();
        this.clazz = clazz.get();
    }

    @Override
    public Token toToken() {
        return classRef.getToken();
    }

    @Override
    public void generateAccess(ASMContext context, ASMWriter writer) {
        writer.writeln("RMEM 1\t;\tReservamos espacio para CIR");
        writer.writeln("PUSH %s\t;\tTamano del CIR a crear", clazz.getAllAttributes().size());
        writer.writeln("PUSH %s", context.getMallocLabel());
        writer.writeln("CALL\t;\tCall malloc");
        writer.writeln("DUP");
        writer.writeln("PUSH %s\t;\tCargar direccion de VT", labelVT(clazz));
        writer.writeln("STOREREF 0\t;\tGuardar direccion de VT en CIR");

        writer.writeln("DUP");
        for (ExpressionNode param : paramsExpressions) {
            param.generate(context, writer);
            writer.writeln("SWAP\t;\tSubimos el this para el constructor");
        }

        writer.writeln("PUSH %s\t;\tCargamos direccion de constructor", label(constructor));
        writer.writeln("CALL\t;\tLlamamos al constructor");
    }
}
