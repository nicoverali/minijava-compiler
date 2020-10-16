package grammar;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class Grammar {

    public static final String LAMBDA = "EOF";

    Multimap<GrammarTerm, GrammarBody> grammar = HashMultimap.create();

    /**
     * Checks whether this Grammar has at leas one production rule or not.
     *
     * @return true if there is at least one production rule present in this Grammar, false otherwise
     */
    public boolean isEmpty(){
        return grammar.isEmpty();
    }

    /**
     * Adds a new production rule where <code>head</code> is the non-terminal term which
     * can be replaced by <code>body</code>.
     *
     * @param head the head of the production rule
     * @param body the body of the production rule
     */
    public void add(GrammarTerm head, GrammarBody body){
        grammar.put(head, body);
    }

    /**
     * For every {@link GrammarBody} given, adds a new production rule where <code>head</code> is the
     * non-terminal term that can be replaced by the {@link GrammarBody}.
     *
     * @param head the head of all the new production rules
     * @param bodies a {@link Collection} of bodies of production rules
     */
    public void addAll(GrammarTerm head, Collection<GrammarBody> bodies){
        grammar.putAll(head, bodies);
    }

    /**
     * Returns a collection of all the production rule bodies which are associated to head {@link GrammarTerm}
     * which name is equal to the given String.
     * If no head have the same name as the String given, then an exception will be thrown.
     *
     * @param head the {@link GrammarTerm} head of all the {@link GrammarBody} returned
     * @return a {@link Collection} of {@link GrammarBody} associated with the given <code>head</code>
     * @throws IllegalArgumentException if the given <code>head</code> does not exist in this Grammar
     */
    public Collection<GrammarBody> get(String head) throws IllegalArgumentException{
        GrammarTerm headTerm = grammar.keySet().stream()
                .filter(term -> term.getName().equals(head)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("The given head ("+head+") does not exist in this Grammar"));
        return grammar.get(headTerm);
    }

    /**
     * Returns a collection of all the production rule bodies associated with the given {@link GrammarTerm}.
     * If the given head does not exist in this Grammar then an exception will be thrown.
     *
     * @param head the {@link GrammarTerm} head of all the {@link GrammarBody} returned
     * @return a {@link Collection} of {@link GrammarBody} associated with the given <code>head</code>
     * @throws IllegalArgumentException if the given <code>head</code> does not exist in this Grammar
     */
    public Collection<GrammarBody> get(GrammarTerm head) throws IllegalArgumentException{
        Collection<GrammarBody> bodies = grammar.get(head);
        if (bodies.isEmpty()) throw new IllegalArgumentException("The requested head ("+head.getName()+") does not exist in this Grammar");
        return bodies;
    }

    /**
     * @return a {@link Collection} of all the {@link GrammarTerm} heads in this Grammar.
     */
    public Collection<GrammarTerm> heads(){
        return grammar.asMap().keySet();
    }

    /**
     * Checks whether the given {@link GrammarTerm} is a leaf of this Grammar.
     * A term is considered a leaf if none of its production rules contain a non-terminal term.
     *
     * @param term the {@link GrammarTerm} of which it will be verified if it is leaf or not
     * @return true if the given {@link GrammarTerm} is a leaf of this Grammar, false otherwise
     */
    public boolean isGrammarLeaf(GrammarTerm term){
        return grammar.get(term).stream()
                .flatMap(GrammarBody::stream)
                .noneMatch(GrammarTerm::isNonTerminal);
    }

//    public Map<GrammarTerm, List<GrammarTerm>> getFirsts(){
//        Map<GrammarTerm, List<GrammarTerm>> firsts = new HashMap<>();
//        for (GrammarTerm head : heads()){
//            firsts.put(head, firstOf(head));
//        }
//        return firsts;
//    }

    /**
     * Returns a {@link List} of all the <b>firsts</b> of the given {@link GrammarTerm}.
     * The definition of <b>firsts</b> can be found here:
     * <a href="https://en.wikipedia.org/wiki/LL_parser#Terminology">Wikipedia: Firsts</a>
     *
     * @param term the {@link GrammarTerm} of which will be computed the firsts
     * @return a {@link List} of <b>firsts</b> of the given {@link GrammarTerm}
     */
    public List<GrammarTerm> firstOf(GrammarTerm term){
        if (term.isNonTerminal()){
            return grammar.get(term).stream()
                    .filter(body -> !body.first().equals(term))
                    .flatMap(body -> firstOf(body).stream())
                    .collect(Collectors.toList());

        } else {
            return Lists.newArrayList(term);
        }
    }

    /**
     * Returns a {@link List} of all the <b>firsts</b> of the given {@link GrammarBody}.
     * The definition of <b>firsts</b> can be found here:
     * <a href="https://en.wikipedia.org/wiki/LL_parser#Terminology">Wikipedia: Firsts</a>
     *
     * @param body the {@link GrammarBody} of which will be computed the firsts
     * @return a {@link List} of <b>firsts</b> of the given {@link GrammarBody}
     */
    @NotNull
    public List<GrammarTerm> firstOf(GrammarBody body){
        if (body.size() == 1) return firstOf(body.first());

        List<GrammarTerm> firsts = new ArrayList<>(firstOf(body.first()));
        //noinspection SuspiciousMethodCalls
        if (firsts.remove(LAMBDA)){
            firsts.addAll(firstOf(body.subrange(1, body.size())));
        }
        return firsts;
    }

    public int getHeadDepth(GrammarTerm head){
        if (isGrammarLeaf(head)){
            return 0;
        }
        return headDepth(head, Lists.newArrayList(head));
    }

    private int headDepth(GrammarTerm head, List<GrammarTerm> visited){
        return get(head).stream()
                .flatMap(GrammarBody::stream)
                .filter(GrammarTerm::isNonTerminal)
                .filter(bodyTerm -> !bodyTerm.equals(head))
                .filter(nt -> !visited.contains(nt))
                .map(nt -> {List<GrammarTerm> currentVisited = new ArrayList<>(visited); currentVisited.add(nt); return currentVisited;})
                .map(currentVisited -> headDepth(Iterables.getLast(currentVisited), currentVisited)+1)
                .max(Integer::compareTo)
                .orElse(0);
    }

}
