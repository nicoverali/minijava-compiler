import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class GrammarVerifierLLOne {

    private static final String LAMBDA = "EOF";

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("grammars/minijava-grammar-left-factored"));
        Multimap<String, List<String>> grammar = getGrammar(reader.lines().toArray(String[]::new));

        // These are not factored left
        Map<String, List<String>> firsts = getFirsts(grammar);
        Map<String, List<String>> notFactored = firsts.entrySet().stream()
                .filter(GrammarVerifierLLOne::isNotLeftFactored).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        System.out.println("These are not factored left ...");
        for (String head : notFactored.keySet()){
            System.out.println("\tHead: " + head);
            System.out.println("\t\t|| Firsts: " + notFactored.get(head));
            System.out.println("\t\t|| Repeated: " + getRepeated(notFactored.get(head)));
            System.out.println("\t\t|| Depth: "+ getTermDepth(head, grammar, Lists.newArrayList(head)));
        }

        // These have left recursion
        Multimap<String, List<String>> leftRecursive = getLeftRecursive(grammar);
        System.out.println("These have left recursion ...");
        for (String head : leftRecursive.asMap().keySet()){
            System.out.println("\tHead: " + head);
            int bodyNum = 1;
            for (List<String> body : leftRecursive.get(head)){
                System.out.println("\t\t|| Body "+(bodyNum++)+": " + body);
            }
        }

    }

    private static Multimap<String, List<String>> getGrammar(String[] lines){
        Multimap<String, List<String>> grammar = HashMultimap.create();
        for (String line : lines){
            String[] lineArray = line.split("\\s+");
            String head = lineArray[0];
            String[] bodies = Arrays.copyOfRange(lineArray, 2, lineArray.length);

            List<List<String>> bodiesList = new ArrayList<>();
            List<String> currentBody = new ArrayList<>();
            for (String term : bodies){
                if (!term.equals("|")){
                    currentBody.add(term);
                }else {
                    bodiesList.add(currentBody);
                    currentBody = new ArrayList<>();
                }
            }
            bodiesList.add(currentBody);
            bodiesList.forEach(body -> grammar.put(head, body));

//            System.out.println("Head: "+head);
//            bodiesList.forEach(body -> System.out.println("Body: "+body));
//            System.out.println("---------");

        }
        return grammar;
    }


    private static Map<String, List<String>> getFirsts(Multimap<String, List<String>> grammar){
        Map<String, List<String>> firsts = new HashMap<>();
        for (String head : grammar.asMap().keySet()){
            firsts.put(head, firstOf(head, grammar));
        }
        return firsts;
    }

    @NotNull
    private static List<String> firstOf(String term, Multimap<String, List<String>> grammar){
        if (isNonTerminal(term)){
            return grammar.get(term).stream()
                    .filter(body -> !body.get(0).equals(term))
                    .flatMap(body -> firstOf(body, grammar).stream())
                    .collect(Collectors.toList());

        } else {
            return Lists.newArrayList(term);
        }
    }

    @NotNull
    private static List<String> firstOf(List<String> body, Multimap<String, List<String>> grammar){
        if (body.size() == 1) return firstOf(body.get(0), grammar);

        List<String> firsts = new ArrayList<>(firstOf(body.get(0), grammar));
        if (firsts.remove(LAMBDA)){
            firsts.addAll(firstOf(body.subList(1, body.size()), grammar));
        }
        return firsts;
    }

    private static boolean isNotLeftFactored(Map.Entry<String, List<String>> headFirsts){
        List<String> firsts = headFirsts.getValue();
        return firsts.stream().distinct().count() != firsts.size();
    }

    private static int getTermDepth(String term, Multimap<String, List<String>> grammar, List<String> visited) {
        if (isGrammarLeaf(term, grammar)){
            return 0;
        } else {
            return grammar.get(term).stream()
                    .flatMap(Collection::stream)
                    .filter(GrammarVerifierLLOne::isNonTerminal)
                    .filter(bodyTerm -> !term.equals(bodyTerm))
                    .filter(nt -> !visited.contains(nt))
                    .map(nt -> {List<String> currentVisited = new ArrayList<>(visited); currentVisited.add(nt); return currentVisited;})
                    .map(currentVisited -> getTermDepth(Iterables.getLast(currentVisited), grammar, currentVisited)+1)
                    .max(Integer::compareTo)
                    .orElse(0);
        }
    }

    private static boolean isGrammarLeaf(String term, Multimap<String, List<String>> grammar){
        return grammar.get(term).stream()
                .flatMap(Collection::stream)
                .noneMatch(GrammarVerifierLLOne::isNonTerminal);
    }

    private static boolean isNonTerminal(String term){
        return term.charAt(0) == '<' && term.charAt(term.length()-1) == '>';
    }

    private static Multimap<String, List<String>> getLeftRecursive(Multimap<String, List<String>> grammar){
        Multimap<String, List<String>> leftRecursive = HashMultimap.create();
        for (String head : grammar.asMap().keySet()){
            List<List<String>> leftRecursiveBodies =
                    grammar.get(head).stream()
                    .filter(body -> body.get(0).equals(head))
                    .collect(Collectors.toList());

            if (!leftRecursiveBodies.isEmpty()){
                leftRecursive.putAll(head, leftRecursiveBodies);
            }
        }
        return leftRecursive;
    }

    private static List<String> getRepeated(List<String> terms){
        List<String> visited = new ArrayList<>();
        List<String> result = new ArrayList<>();
        for (String term : terms){
            if (visited.contains(term)){
                visited.add(term);
                result.add(term);
            } else {
                visited.add(term);
            }
        }
        return result.stream().distinct().collect(Collectors.toList());
    }
}
