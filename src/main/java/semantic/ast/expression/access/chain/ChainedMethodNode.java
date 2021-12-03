package semantic.ast.expression.access.chain;

import asm.ASMLabeler;
import asm.ASMWriter;
import lexical.Token;
import semantic.SemanticException;
import semantic.ast.asm.ASMContext;
import semantic.ast.expression.access.AccessNode;
import semantic.ast.scope.Scope;
import semantic.ast.expression.ExpressionNode;
import semantic.symbol.ClassSymbol;
import semantic.symbol.finder.MethodFinder;
import semantic.symbol.MethodSymbol;
import semantic.symbol.SymbolTable;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.Type;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static asm.ASMLabeler.label;
import static semantic.symbol.attribute.IsStaticAttribute.emptyDynamic;
import static semantic.symbol.attribute.type.VoidType.VOID;

public class ChainedMethodNode extends BaseChainNode {

    private final SymbolTable ST = SymbolTable.getInstance();

    private final NameAttribute name;
    private final List<ExpressionNode> paramsExps;

    private MethodSymbol methodSymbol;

    public ChainedMethodNode(AccessNode leftAccess, Token dotToken, NameAttribute name, List<ExpressionNode> paramsExps) {
        super(leftAccess, dotToken);
        this.name = name;
        this.paramsExps = paramsExps;
    }

    public ChainedMethodNode(AccessNode leftAccess, Token dotToken, NameAttribute name, List<ExpressionNode> paramsExps, ChainNode chain) {
        super(leftAccess, dotToken, chain);
        this.name = name;
        this.paramsExps = paramsExps;
    }

    @Override
    public Type getAccessType() {
        return methodSymbol.getReturnType();
    }

    @Override
    public void validateAccess(Scope scope) {
        ClassSymbol classSym = validateLeftAccess();

        // Validate parameters and get the type of them
        paramsExps.forEach(exp -> exp.validate(scope));
        List<Type> paramsType = paramsExps.stream().map(ExpressionNode::getType).collect(Collectors.toList());

        // Find this method within the left referenced class
        Optional<MethodSymbol> method = new MethodFinder(classSym).find(name, paramsType);
        if (method.isEmpty()){
            // Method is not declared or not suitable parameters found
            if (new MethodFinder(classSym).find(emptyDynamic(), name).isEmpty()){
                throw new SemanticException("Metodo no declarado", name);
            } else {
                throw new SemanticException("No se pudo encontrar un metodo adecuado para los parametros dados", name);
            }
        }
        methodSymbol = method.get();
    }

    @Override
    public Token toToken() {
        return name.getToken();
    }

    @Override
    public void generateAccess(ASMContext context, ASMWriter writer) {
        if (methodSymbol.isStatic()) {
            generateStaticAccess(context, writer);
        } else {
            generateDynamicAccess(context, writer);
        }
    }

    private void generateStaticAccess(ASMContext context, ASMWriter writer) {
        writer.writeln("FMEM 1\t;\tDescartamos la referencia previa porque el metodo es estatico");

        if (!methodSymbol.getReturnType().equals(VOID())) {
            writer.writeln("RMEM 1\t;\tReservar espacio para valor de retorno");
        }

        paramsExps.forEach(param -> param.generate(context, writer));

        writer.writeln("PUSH %s\t;\tCargamos direccion de metodo estatico", label(methodSymbol));
        writer.writeln("CALL\t;\tLlamada a metodo encadenado");
    }

    private void generateDynamicAccess(ASMContext context, ASMWriter writer) {
        if (!methodSymbol.getReturnType().equals(VOID())) {
            writer.writeln("RMEM 1\t;\tReservar espacio para valor de retorno");
            writer.writeln("SWAP\t;\tSubimos this del nuevo RA");
        }

        for (ExpressionNode param : paramsExps) {
            param.generate(context, writer);
            writer.writeln("SWAP\t;\tSubimos this del nuevo RA");
        }

        writer.writeln("DUP\t;\tDuplicamos this para buscar en la VT");
        writer.writeln("LOADREF 0\t;\tLoad VT");
        writer.writeln("LOADREF %s\t;\tLoad direccion del metodo", context.getOffsetOf(methodSymbol));
        writer.writeln("CALL\t;\tLlamada a metodo encadenado");
    }

}
