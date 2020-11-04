package semantic.symbol.predefined;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * A predefined class is inserted into the {@link semantic.symbol.SymbolTable} at initialization of the program.
 * It defines the name and methods that will already exist at the beginning, so that classes and methods from
 * a source code can call them of make references to them.
 */
public class PredefinedClass {

    private final String name;
    private final List<PredefinedMethod> methods = new ArrayList<>();

    public PredefinedClass(String name){
        this.name = name;
    }

    /**
     * Adds a new {@link PredefinedMethod} to this predefined class.
     *
     * @param method a {@link PredefinedMethod} which will be added to this predefined class
     */
    public void add(PredefinedMethod method){
        methods.add(method);
    }

    /**
     * @return the name of this predefined class as a {@link String}
     */
    public String getName(){
        return name;
    }

    /**
     * @return a collection of all the {@link PredefinedMethod} of this predefined class
     */
    public Collection<PredefinedMethod> getMethods(){
        return methods;
    }

    /**
     * Returns a {@link PredefinedMethod} which belongs to this predefined class and has the
     * same name as the one given as argument.
     *
     * @param name the name of the returned method
     * @return an {@link Optional} wrapping the matched {@link PredefinedMethod}
     */
    public Optional<PredefinedMethod> getMethod(String name){
        return methods.stream()
                .filter(method -> name.equals(method.getName()))
                .findFirst();
    }

}
