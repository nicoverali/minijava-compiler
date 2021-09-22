package grammar.generator;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a body inside a {@link GeneratorMethod}. This body has a list of tokens that need
 * to be matched to access this body actions.
 */
public class GeneratorMethodBody {

    private final List<String> predicateTokens = new ArrayList<>();
    private final List<String> actions = new ArrayList<>();


    /**
     * @return true if this body has no predicate tokens;
     */
    public boolean isEmpty(){
        return predicateTokens.isEmpty();
    }

    /**
     * The predicate tokens of the body are the tokens that must be matched to access this generator actions.
     *
     * @return a {@link List} of all the predicate tokens of this body
     */
    public List<String> getPredicateTokens() {
        return predicateTokens;
    }

    /**
     * Adds a new predicate token to this body.
     *
     * @param token a token which will be added as a predicate token to this body
     */
    public void addToken(String token) {
        predicateTokens.add(token);
    }

    /**
     * A body action is an instruction that must be run if this body is access. The actions must be run in the
     * same order as they are given by this method.
     *
     * @return a {@link List} of all the actions in this body
     */
    public List<String> getActions() {
        return actions;
    }

    /**
     * Adds a new token action to this body. Token actions are the ones that try to match the current token in the
     * analyzer with a certain token, if they don't match then a SyntacticException
     *
     * @param token the token of the new token action
     */
    public void addTokenAction(String token) {
        actions.add("match("+token+");");
    }

    /**
     * Adds a new non-terminal action. Non-terminal actions result in a call to another method.
     *
     * @param name the name of the non-terminal term
     */
    public void addNonTerminalAction(String name){
        actions.add(name+"();");
    }

    public String toString(){
        return "ifAny("+ Joiner.on(',').join(predicateTokens) +") {";
    }
}
