package semantic.symbol;

import semantic.SemanticException;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.predefined.PredefinedClass;
import semantic.symbol.predefined.PredefinedMethod;
import semantic.symbol.predefined.PredefinedParameter;

import java.util.HashMap;
import java.util.Map;

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
    public ClassSymbol currentClass;

    private final Map<String, PredefinedClass> predefineClasses = new HashMap<>();
    private final Map<String, ClassSymbol> classes = new HashMap<>();
    private final Map<String, InterfaceSymbol> interfaces = new HashMap<>();


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
        predefineClasses.put(SYSTEM_CLASSNAME, system);
        predefineClasses.put(OBJECT_CLASSNAME, object);
    }

    /**
     * Adds a new {@link ClassSymbol} to this symbol table.
     * If the symbol table already had a {@link ClassSymbol} with the same name, then an exception will be thrown
     *
     * @param c a {@link ClassSymbol} which will be added to this symbol table
     * @throws SemanticException if the table already had a {@link ClassSymbol} with the same name as the new one
     */
    public void add(ClassSymbol c) throws SemanticException{
        NameAttribute className = c.getName();
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
        NameAttribute interfaceName = i.getName();
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
