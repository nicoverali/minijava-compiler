package semantic.symbol;

import lexical.Token;
import semantic.SemanticException;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.ReferenceType;
import semantic.symbol.predefined.PredefinedClass;
import semantic.symbol.predefined.PredefinedMethod;
import semantic.symbol.predefined.PredefinedParameter;
import semantic.symbol.user.UserClassSymbol;

import java.util.*;

import static semantic.symbol.attribute.type.PrimitiveType.*;
import static semantic.symbol.attribute.type.VoidType.VOID;

public class SymbolTable {

    public static final String OBJECT_CLASSNAME = "Object";
    public static final String SYSTEM_CLASSNAME = "System";

    private SymbolTable(){
        createPredefinedClasses();
    }

    private static class SingletonHelper{
        private static final SymbolTable INSTANCE = new SymbolTable();
    }

    public static SymbolTable getInstance(){
        return SingletonHelper.INSTANCE;
    }

    public UserClassSymbol currentClass;

    private final Map<String, PredefinedClass> predefineClasses = new HashMap<>();
    private final Map<String, UserClassSymbol> classes = new HashMap<>();

    private final ReferenceType defaultClass = new ReferenceType(OBJECT_CLASSNAME);

    private MethodSymbol mainMethod;

    private void createPredefinedClasses() {
        PredefinedClass object = new PredefinedClass(OBJECT_CLASSNAME);
        object.add(PredefinedMethod.createStatic(VOID(), "debugPrint", new PredefinedParameter(INT(), "i")));

        PredefinedClass system = new PredefinedClass(SYSTEM_CLASSNAME);
        system.add(PredefinedMethod.createStatic(INT(), "read"));
        system.add(PredefinedMethod.createStatic(VOID(), "printB", new PredefinedParameter(BOOLEAN(), "b")));
        system.add(PredefinedMethod.createStatic(VOID(), "printC", new PredefinedParameter(CHAR(), "c")));
        system.add(PredefinedMethod.createStatic(VOID(), "printI", new PredefinedParameter(INT(), "i")));
        system.add(PredefinedMethod.createStatic(VOID(), "printS", new PredefinedParameter(STRING(), "s")));
        system.add(PredefinedMethod.createStatic(VOID(), "println"));
        system.add(PredefinedMethod.createStatic(VOID(), "printBln", new PredefinedParameter(BOOLEAN(), "b")));
        system.add(PredefinedMethod.createStatic(VOID(), "printCln", new PredefinedParameter(CHAR(), "c")));
        system.add(PredefinedMethod.createStatic(VOID(), "printIln", new PredefinedParameter(INT(), "i")));
        system.add(PredefinedMethod.createStatic(VOID(), "printSln", new PredefinedParameter(STRING(), "s")));
        system.setParent(defaultClass);

        predefineClasses.put(SYSTEM_CLASSNAME, system);
        predefineClasses.put(OBJECT_CLASSNAME, object);
    }

    /**
     * Adds a new {@link UserClassSymbol} to this symbol table.
     * If the symbol table already had a {@link UserClassSymbol} with the same name, then an exception will be thrown
     *
     * @param c a {@link UserClassSymbol} which will be added to this symbol table
     * @throws SemanticException if the table already had a {@link UserClassSymbol} with the same name as the new one
     */
    public void add(UserClassSymbol c) throws SemanticException{
        NameAttribute className = c.getNameAttribute();
        checkForDuplicates(className);
        classes.put(className.getValue(), c);
    }

    /**
     * Resets this symbol table to its initial state, clearing all classes and interfaces
     */
    public void reset(){
        classes.clear();
        currentClass = null;
    }

    /**
     * @return a {@link Collection} with all the classes of this table, including both user classes
     * and predefined classes
     */
    public Collection<ClassSymbol> getAllClasses(){
        Collection<ClassSymbol> allClasses = new ArrayList<>(classes.values());
        allClasses.addAll(predefineClasses.values());
        return allClasses;
    }

    /**
     * @param reference a reference of the {@link ClassSymbol} to look for
     * @return an {@link Optional} wrapping a {@link ClassSymbol} from this table which was pointed by the given reference
     */
    public Optional<ClassSymbol> getClass(ReferenceType reference){
        return getUserClass(reference)
                .map(ucs -> (ClassSymbol) ucs)
                .or(() -> getPredefinedClass(reference));
    }

    /**
     * @param reference a reference of the {@link UserClassSymbol} to look for
     * @return an {@link Optional} wrapping a {@link UserClassSymbol} from this table which was pointed by the given reference
     */
    public Optional<UserClassSymbol> getUserClass(ReferenceType reference){
        return Optional.ofNullable(classes.get(reference.getValue()));
    }

    /**
     * @param reference a reference of the {@link PredefinedClass} to look for
     * @return an {@link Optional} wrapping a {@link PredefinedClass} from this table which was pointed by the given reference
     */
    public Optional<PredefinedClass> getPredefinedClass(ReferenceType reference){
        return Optional.ofNullable(predefineClasses.get(reference.getValue()));
    }

    /**
     * Returns the main method in this table. This method must be called after the table has been
     * consolidated, an exception will be thrown otherwise
     *
     * @return the main method of this table
     */
    public MethodSymbol getMainMethod(){
        if (mainMethod == null) throw new IllegalStateException("Table must be consolidated before calling this method");
        return mainMethod;
    }

    /**
     * @return the default {@link UserClassSymbol} of all classes as a {@link ReferenceType}
     */
    public ReferenceType getDefaultClass(){
        return defaultClass;
    }

    /**
     * Goes through all the table, checking if all declarations are valid.
     * If a semantic error is detected, then an exception will be thrown
     *
     * @throws SemanticException if a semantic error is detected during consolidation
     */
    public void consolidate() throws SemanticException {
        classes.values().forEach(ClassSymbol::checkDeclaration);
        SymbolTreeValidator.checkCircularInheritance(classes);
        classes.values().forEach(ClassSymbol::consolidate);
        mainMethod = SymbolTreeValidator.checkMainMethod(classes);

        classes.values().forEach(ClassSymbol::checkSentences);
    }

    private void checkForDuplicates(NameAttribute name) throws SemanticException{
        String nameValue = name.getValue();
        Token token = name.getToken();
        if (classes.containsKey(nameValue)){
            throw new SemanticException("El programa no puede contener dos clases con el mismo nombre", token);
        }
        if (predefineClasses.containsKey(nameValue)){
            throw new SemanticException("No es posible declarar una clase o interfaz "+nameValue+" porque es una clase predefinida del sistema", token);
        }
    }

}
