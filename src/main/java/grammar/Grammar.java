package grammar;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Grammar {

    private final String lambda;

    Multimap<String, List<String>> grammar = HashMultimap.create();

    public Grammar(String lambda) {
        this.lambda = lambda;
    }

    public boolean isEmpty(){
        return grammar.isEmpty();
    }

    public void add(String head, List<String> body){
        grammar.put(head, body);
    }

    public void addAll(String head, Collection<List<String>> bodies){
        grammar.putAll(head, bodies);
    }

    public Collection<List<String>> get(String head){
        return grammar.get(head);
    }

    public Collection<String> heads(){
        return grammar.asMap().keySet();
    }

    public boolean isNonTerminal(String term){
        return term.charAt(0) == '<' && term.charAt(term.length()-1) == '>';
    }

    public boolean isGrammarLeaf(String term){
        return grammar.get(term).stream()
                .flatMap(Collection::stream)
                .noneMatch(this::isNonTerminal);
    }

    public Map<String, List<String>> getFirsts(){
        Map<String, List<String>> firsts = new HashMap<>();
        for (String head : heads()){
            firsts.put(head, firstOf(head));
        }
        return firsts;
    }

    @NotNull
    public List<String> firstOf(String term){
        if (isNonTerminal(term)){
            return grammar.get(term).stream()
                    .filter(body -> !body.get(0).equals(term))
                    .flatMap(body -> firstOf(body).stream())
                    .collect(Collectors.toList());

        } else {
            return Lists.newArrayList(term);
        }
    }

    @NotNull
    private List<String> firstOf(List<String> body){
        if (body.size() == 1) return firstOf(body.get(0));

        List<String> firsts = new ArrayList<>(firstOf(body.get(0)));
        if (firsts.remove(lambda)){
            firsts.addAll(firstOf(body.subList(1, body.size())));
        }
        return firsts;
    }

    public int getHeadDepth(String head) {
        if (isGrammarLeaf(head)){
            return 0;
        }
        return headDepth(head, Lists.newArrayList(head));
    }

    private int headDepth(String head, List<String> visited){
        return get(head).stream()
                .flatMap(Collection::stream)
                .filter(this::isNonTerminal)
                .filter(bodyTerm -> !head.equals(bodyTerm))
                .filter(nt -> !visited.contains(nt))
                .map(nt -> {List<String> currentVisited = new ArrayList<>(visited); currentVisited.add(nt); return currentVisited;})
                .map(currentVisited -> headDepth(Iterables.getLast(currentVisited), currentVisited)+1)
                .max(Integer::compareTo)
                .orElse(0);
    }

}
