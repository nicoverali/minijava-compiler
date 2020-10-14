package grammar;

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

    private static final String FILE_PATH = "grammars/minijava-grammar-left-factored";
    private static final String LAMBDA = "EOF";
    private static final Grammar GRAMMAR = new GrammarReader(FILE_PATH, LAMBDA).getGrammar();

    public static void main(String[] args) {
        boolean isLL1 = true;

        // These are not factored left
        Map<String, List<String>> notFactored = getAllNotLeftFactored();
        if (!notFactored.isEmpty()){
            isLL1 = false;
            System.out.println("These are not factored left ...");
            for (String head : notFactored.keySet()){
                System.out.println("\tHead: " + head);
                System.out.println("\t\t|| Firsts: " + notFactored.get(head));
                System.out.println("\t\t|| Repeated: " + getRepeated(notFactored.get(head)));
                System.out.println("\t\t|| Depth: "+ GRAMMAR.getHeadDepth(head));
            }
        }

        // These have left recursion
        Grammar leftRecursiveSubGrammar = getLeftRecursive();
        if (!leftRecursiveSubGrammar.isEmpty()){
            isLL1 = false;
            System.out.println("These have left recursion ...");
            for (String head : leftRecursiveSubGrammar.heads()){
                System.out.println("\tHead: " + head);
                int bodyNum = 1;
                for (List<String> body : leftRecursiveSubGrammar.get(head)){
                    System.out.println("\t\t|| Body "+(bodyNum++)+": " + body);
                }
            }
        }

        if (isLL1){
            System.out.println("Congratulations !!! Your grammar is LL1.");
        }

    }

    private static Map<String, List<String>> getAllNotLeftFactored(){
        return GRAMMAR.getFirsts().entrySet().stream()
                .filter(GrammarVerifierLLOne::isNotLeftFactored)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static boolean isNotLeftFactored(Map.Entry<String, List<String>> headFirsts){
        List<String> firsts = headFirsts.getValue();
        return firsts.stream().distinct().count() != firsts.size();
    }

    private static Grammar getLeftRecursive(){
        Grammar leftRecursive = new Grammar(LAMBDA);
        for (String head : GRAMMAR.heads()){
            List<List<String>> leftRecursiveBodies =
                    GRAMMAR.get(head).stream()
                    .filter(body -> body.get(0).equals(head))
                    .collect(Collectors.toList());

            if (!leftRecursiveBodies.isEmpty()){
                leftRecursive.addAll(head, leftRecursiveBodies);
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
