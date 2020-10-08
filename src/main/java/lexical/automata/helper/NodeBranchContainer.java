package lexical.automata.helper;

import io.code.CodeCharacter;
import lexical.automata.NodeBranch;

import java.util.*;

/**
 * This is a helper class that stores multiple {@link NodeBranch} and provides methods
 * to simplify interactions with them as group.
 *
 * @param <T> type of branches
 */
public class NodeBranchContainer<T extends NodeBranch<?>> {

    private final List<T> branches = new ArrayList<>();

    /**
     * Adds a {@link NodeBranch} to this container
     *
     * @param branch branch to add to this container
     */
    public void addBranch(T branch){
        branches.add(branch);
    }

    /**
     * Test every branch in this container with the given <code>character</code>. Returns the first branch that matches
     * with the <code>character</code>. If no branch matches the <code>character</code> an empty {@link Optional} will
     * be returned.
     * <br><br>
     * If more than one branch matches the <code>character</code>, then one of them will be
     * selected in no particular order.
     *
     * @param character a char value to test every branch
     * @return an {@link Optional} wrapping a matching branch
     */
    public Optional<T> getMatchingBranch(CodeCharacter character){
        for (T branch : branches){
            if (branch.getFilter().test(character.getValue())){
                return Optional.of(branch);
            }
        }
        return Optional.empty();
    }

    /**
     * Test every branch in this container with the given <code>character</code>. Returns all the branches that matches
     * with the <code>character</code> as a {@link Queue}.
     * <br><br>
     * Matching branches will be returned in no particular order.
     *
     * @param character a char value to test every branch
     * @return a {@link Queue} containing all matching branches
     */
    public Queue<T> getAllMatchingBranches(CodeCharacter character){
        Queue<T> matching = new ArrayDeque<>(branches.size());
        for (T branch : branches){
            if (branch.getFilter().test(character.getValue())){
                matching.add(branch);
            }
        }
        return matching;
    }


}
