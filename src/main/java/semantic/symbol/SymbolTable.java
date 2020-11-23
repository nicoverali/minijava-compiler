package semantic.symbol;

import lexical.Token;
import semantic.SemanticException;
import semantic.SymbolTreeValidator;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.ReferenceType;
import semantic.symbol.predefined.PredefinedClass;
import semantic.symbol.predefined.PredefinedMethod;
import semantic.symbol.predefined.PredefinedParameter;
import semantic.symbol.user.UserClassSymbol;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

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

    public InterfaceSymbol currentInterface;
    public UserClassSymbol currentClass;

    private final Map<String, PredefinedClass> predefineClasses = new HashMap<>();
    private final Map<String, UserClassSymbol> classes = new HashMap<>();
    private final Map<String, InterfaceSymbol> interfaces = new HashMap<>();

    private final ReferenceType defaultClass = new ReferenceType(OBJECT_CLASSNAME);

    private void createPredefinedClasses() {
        PredefinedClass object = new PredefinedClass(OBJECT_CLASSNAME);
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
     * Adds a new {@link InterfaceSymbol} to this symbol table
     * If the symbol table already had an {@link InterfaceSymbol} with the same name,
     * then an exception will be thrown
     *
     * @param i a {@link InterfaceSymbol} which will be added to this symbol table
     * @throws SemanticException if the table already had an {@link InterfaceSymbol} with the same name as the new one
     */
    public void add(InterfaceSymbol i){
        NameAttribute interfaceName = i.getNameAttribute();
        checkForDuplicates(interfaceName);
        interfaces.put(interfaceName.getValue(), i);
    }

    /**
     * Resets this symbol table to its initial state, clearing all classes and interfaces
     */
    public void reset(){
        classes.clear();
        interfaces.clear();
        currentClass = null;
        currentInterface = null;
    }

    /**
     * @param reference a reference of the {@link ClassSymbol} to look for
     * @return an {@link Optional} wrapping a {@link ClassSymbol} from this table which was pointed by the given reference
     */
    public Optional<ClassSymbol> getClass(ReferenceType reference){
        ClassSymbol userClass = classes.get(reference.getValue());
        if(userClass != null){
            return Optional.of(userClass);
        }
        return Optional.ofNullable(predefineClasses.get(reference.getValue()));
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
     * @return the default {@link UserClassSymbol} of all classes as a {@link ReferenceType}
     */
    public ReferenceType getDefaultClass(){
        return defaultClass;
    }

    /**
     * @param reference a reference of the {@link InterfaceSymbol} to look for
     * @return an {@link Optional} wrapping a {@link InterfaceSymbol} from this table which was pointed by the given reference
     */
    public Optional<InterfaceSymbol> getInterface(ReferenceType reference){
        return Optional.ofNullable(interfaces.get(reference.getValue()));
    }

    /**
     * Searches through all the table looking for a {@link TopLevelSymbol} which is pointed by the given reference
     *
     * @param reference a reference of the symbol to look for
     * @return an {@link Optional} wrapping a {@link TopLevelSymbol} which was pointed by the given reference
     */
    public Optional<TopLevelSymbol> getTopLevelSymbol(ReferenceType reference){
        return getTopLevelSymbol(reference.getValue());
    }

    /**
     * Searches through all the table looking for a {@link TopLevelSymbol} that has the same name as the one given
     *
     * @param name the name of the symbol to look for
     * @return an {@link Optional} wrapping a {@link TopLevelSymbol} that has the same name as the one given
     */
    public Optional<TopLevelSymbol> getTopLevelSymbol(String name){
        return Stream.of(classes, interfaces, predefineClasses)
                .map(symbolMap -> (TopLevelSymbol) symbolMap.get(name))
                .filter(Objects::nonNull)
                .findFirst();
    }

    /**
     * Checks whether the given name is the name of one of the {@link UserClassSymbol} in this table.
     * If no {@link UserClassSymbol} matches the given name then it will also return false.
     * <br>
     * This search will also include any {@link PredefinedClass} present in this table.
     *
     * @param reference the name of the {@link UserClassSymbol} to look for
     * @return true if the given name is of one of the {@link UserClassSymbol} in this table, false otherwise
     */
    public boolean isAClass(ReferenceType reference) {
        return classes.get(reference.getValue()) != null || predefineClasses.get(reference.getValue()) != null;
    }

    /**
     * Checks whether the given name is the name of one of the {@link InterfaceSymbol} in this table.
     * If no {@link InterfaceSymbol} matches the given name then it will also return false.
     *
     * @param reference the name of the {@link InterfaceSymbol} to look for
     * @return true if the given name is of one of the {@link InterfaceSymbol} in this table, false otherwise
     */
    public boolean isAnInterface(ReferenceType reference) {
        return interfaces.get(reference.getValue()) != null;
    }

    /**
     * Goes through all the table, checking if all declarations are valid.
     * If a semantic error is detected, then an exception will be thrown
     *
     * @throws SemanticException if a semantic error is detected during consolidation
     */
    public void consolidate() throws SemanticException {
        interfaces.values().forEach(InterfaceSymbol::checkDeclaration);
        classes.values().forEach(ClassSymbol::checkDeclaration);
        interfaces.values().forEach(InterfaceSymbol::consolidate);
        classes.values().forEach(ClassSymbol::consolidate);
    }

    private void checkForDuplicates(NameAttribute name) throws SemanticException{
        String nameValue = name.getValue();
        Token token = name.getToken();
        if (classes.containsKey(nameValue) || interfaces.containsKey(nameValue)){
            throw new SemanticException("El programa no puede contener dos clases o interfaces con el mismo nombre", token);
        }
        if (predefineClasses.containsKey(nameValue)){
            throw new SemanticException("No es posible declarar una clase o interfaz "+nameValue+" porque es una clase predefinida del sistema", token);
        }
    }

}
