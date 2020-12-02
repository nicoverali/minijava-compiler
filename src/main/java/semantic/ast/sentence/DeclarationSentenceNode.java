package semantic.ast.sentence;

import lexical.Token;
import semantic.SemanticException;
import semantic.ast.Scope;
import semantic.ast.LocalVariable;
import semantic.ast.expression.ExpressionNode;
import semantic.ast.sentence.visitor.SentenceVisitor;
import semantic.symbol.TopLevelSymbol;
import semantic.symbol.attribute.type.Type;
import util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeclarationSentenceNode implements SentenceNode {

    private final Type declarationType;
    private final Map<String, LocalVariable> declarations = new HashMap<>();
    private final Map<LocalVariable, Pair<Token, ExpressionNode>> assignments = new HashMap<>();

    private boolean hasGenerics = false;
    private Scope currScope = null;

    public DeclarationSentenceNode(Type declarationType) {
        this.declarationType = declarationType;
    }

    /**
     * Adds a declared {@link LocalVariable} to this sentence.
     *
     * @param var a {@link LocalVariable} which will be added to this sentence
     * @throws SemanticException if the given variable does already exist in this sentence
     */
    public void add(LocalVariable var) throws SemanticException{
        String name = var.getNameAttribute().getValue();
        if (declarations.containsKey(name)){
            throw new SemanticException("La variable ya fue declarada", var.getNameAttribute());
        }
        declarations.put(name, var);
    }

    /**
     * Adds a declared {@link LocalVariable} and its initial assignment to this sentence.
     *
     * @param var a {@link LocalVariable} which will be added to this sentence
     * @param assignToken the {@link Token} of the assignment symbol
     * @param expression the initial value of the variable as a {@link ExpressionNode}
     * @throws SemanticException if the given variable does already exist in this sentence
     */
    public void add(LocalVariable var, Token assignToken, ExpressionNode expression) throws SemanticException{
        String name = var.getNameAttribute().getValue();
        if (declarations.containsKey(name)){
            throw new SemanticException("La variable ya fue declarada", var.getNameAttribute());
        }
        declarations.put(name, var);
        assignments.put(var, Pair.of(assignToken, expression));
    }

    @Override
    public void validate(Scope scope) {
        currScope = scope;
        checkDuplicates(scope);
        declarations.values().forEach(var -> var.validate(scope));
        assignments.forEach(this::validateAssignments);
        checkGenerics(scope);
        currScope = null;
    }

    private void checkDuplicates(Scope scope) {
        for (LocalVariable var : declarations.values()){
            if (scope.findVariable(var.getNameAttribute()).isPresent()){
                throw new SemanticException("La variable ya fue declarada en este scope", var.getNameAttribute());
            }
            scope.addLocalVariable(var);
        }
    }

    private void checkGenerics(Scope scope) {
        hasGenerics = declarations.values().stream().anyMatch(var -> var.hasGenerics(scope));
        hasGenerics = hasGenerics || assignments.values().stream()
                                        .anyMatch(expPair -> expPair.right.hasGenerics(scope));
    }

    private void validateAssignments(LocalVariable localVariable, Pair<Token, ExpressionNode> assignment) {
        Token token = assignment.left;
        ExpressionNode expression = assignment.right;
        if (!localVariable.getType().equals(expression.getType(currScope))){
            throw new SemanticException("La asignacion inicial no conforma con el tipo de variable", token);
        }
    }

    @Override
    public void accept(SentenceVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public SentenceNode instantiate(TopLevelSymbol container, String newType) {
        if (hasGenerics){
            DeclarationSentenceNode newNode = new DeclarationSentenceNode(declarationType);
            for (LocalVariable var : declarations.values()){
                if (assignments.containsKey(var)){
                    Pair<Token, ExpressionNode> pair = assignments.get(var);
                    newNode.add(var, pair.left, pair.right);
                } else {
                    newNode.add(var);
                }
            }
            return newNode;
        }
        return this;
    }

    @Override
    public boolean hasGenerics(TopLevelSymbol container) {
        return hasGenerics;
    }

}
