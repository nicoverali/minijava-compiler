package grammar;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class Grammar {

    private final String lambda;

    Multimap<GrammarTerm, GrammarBody> grammar = HashMultimap.create();

    public Grammar(String lambda) {
        this.lambda = lambda;
    }

    public boolean isEmpty(){
        return grammar.isEmpty();
    }

    public void add(GrammarTerm head, GrammarBody body){
        grammar.put(head, body);
    }

    public void addAll(GrammarTerm head, Collection<GrammarBody> bodies){
        grammar.putAll(head, bodies);
    }

    public Collection<GrammarBody> get(String head){
        return grammar.get(new GrammarTerm(head));
    }

    public Collection<GrammarBody> get(GrammarTerm head){
        return grammar.get(head);
    }

    public Collection<GrammarTerm> heads(){
        return grammar.asMap().keySet();
    }

    public boolean isGrammarLeaf(String term){
        return isGrammarLeaf(new GrammarTerm(term));
    }

    public boolean isGrammarLeaf(GrammarTerm term){
        return grammar.get(term).stream()
                .flatMap(GrammarBody::stream)
                .noneMatch(GrammarTerm::isNonTerminal);
    }

    public Map<GrammarTerm, List<GrammarTerm>> getFirsts(){
        Map<GrammarTerm, List<GrammarTerm>> firsts = new HashMap<>();
        for (GrammarTerm head : heads()){
            firsts.put(head, firstOf(head));
        }
        return firsts;
    }

    @NotNull
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

    @NotNull
    private List<GrammarTerm> firstOf(GrammarBody body){
        if (body.size() == 1) return firstOf(body.first());

        List<GrammarTerm> firsts = new ArrayList<>(firstOf(body.first()));
        //noinspection SuspiciousMethodCalls
        if (firsts.remove(lambda)){
            firsts.addAll(firstOf(body.subrange(1, body.size())));
        }
        return firsts;
    }

    public int getHeadDepth(String head) {
        return getHeadDepth(new GrammarTerm(head));
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
