package grammar.generator;


import java.util.*;

/**
 * This class represents a method that will be generated by the GrammarGenerator.
 * This includes the name of the method, its body, and a few more details.
 */
public class GeneratorMethod implements Iterable<GeneratorMethodBody> {

    private final String name;
    private final List<GeneratorMethodBody> methodBodies = new ArrayList<>();
    private boolean hasLambda;
    private Set<String> followTokens = Collections.emptySet();

    public GeneratorMethod(String name) {
        this.name = name;
    }

    /**
     * @return the name of this method
     */
    public String getName() {
        return name;
    }

    /**
     * Adds a new {@link GeneratorMethodBody} to this method.
     * @param body a {@link GeneratorMethodBody} which will be added to this method
     */
    public void addMethodBody(GeneratorMethodBody body){
        if (!body.isEmpty()){
            methodBodies.add(body);
        }
    }

    /**
     * @return a {@link List} of all the {@link GeneratorMethodBody} in this method
     */
    public List<GeneratorMethodBody> getMethodBodies() {
        return methodBodies;
    }

    /**
     * @return true if this method has exactly one {@link GeneratorMethodBody}, false otherwise
     */
    public boolean hasSingleBody(){
        return methodBodies.size() == 1;
    }

    /**
     * Confirms that this {@link GeneratorMethod} has a Lambda in one of its bodies.
     * This will later be retrieved with {@link #hasLambda()}.
     */
    public void confirmLambda(){
        hasLambda = true;
    }

    /**
     * @return true if this method has Lambda in one of its bodies, false otherwise
     */
    public boolean hasLambda() {
        return hasLambda;
    }


    public String toString(){
        return "void "+name+"() { // Has LAMBDA: "+hasLambda;
    }

    @Override
    public Iterator<GeneratorMethodBody> iterator() {
        return methodBodies.iterator();
    }

    /**
     * @return a {@link Set} of all the tokens that follow the non-terminal term represented
     * by this method.
     */
    public Set<String> getFollowTokens() {
        return Collections.unmodifiableSet(followTokens);
    }

    /**
     * Sets the list of tokens that follow the non-terminal term represented by this method
     */
    public void setFollowTokens(Set<String> followTokens) {
        this.followTokens = followTokens;
    }
}
