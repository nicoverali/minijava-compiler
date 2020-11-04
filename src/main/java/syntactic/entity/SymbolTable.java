package syntactic.entity;

import syntactic.entity.predefined.PredefinedClass;
import syntactic.entity.predefined.PredefinedMethod;
import syntactic.entity.predefined.PredefinedParameter;

import java.util.HashMap;
import java.util.Map;

import static syntactic.entity.attribute.type.PrimitiveType.*;
import static syntactic.entity.attribute.type.VoidType.VOID;

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
     * Adds a new {@link ClassSymbol} to this symbol table
     * @param c a {@link ClassSymbol} which will be added to this symbol table
     */
    public void add(ClassSymbol c){
        classes.put(c.getName().getValue(), c);
    }

    /**
     * Adds a new {@link InterfaceSymbol} to this symbol table
     * @param i a {@link InterfaceSymbol} which will be added to this symbol table
     */
    public void add(InterfaceSymbol i){
        interfaces.put(i.getName().getValue(), i);
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

}
