package grammar.verifier;

import com.google.common.collect.Iterables;
import grammar.Grammar;
import grammar.GrammarBody;
import grammar.io.GrammarReader;
import grammar.GrammarTerm;

import java.util.*;
import java.util.stream.Collectors;

public class LLOneGrammarVerifier {

    private static final String FILE_PATH = "grammars/minijava-grammar";
    private static final String LAMBDA = "EOF";
    private static final Grammar GRAMMAR = new GrammarReader(FILE_PATH, LAMBDA, new LLOneVerifierGrammarValidator()).getGrammar();

    public static void main(String[] args) {
        boolean isLL1 = true;

        // These are not factored left
        System.out.println("These are not factored left ...");
        for (GrammarTerm head : GRAMMAR.heads()){
            List<GrammarTerm> firsts = GRAMMAR.firstOf(head);
            if (isNotLeftFactored(firsts)){
                isLL1 = false;
                System.out.println("\tHead: " + head);
                System.out.println("\t\t|| Firsts: " + firsts);
                System.out.println("\t\t|| Repeated: " + getRepeated(firsts));
                System.out.println("\t\t|| Depth: "+ GRAMMAR.getHeadDepth(head));
            }
        }

        // These have left recursion
        Grammar leftRecursiveSubGrammar = getLeftRecursive();
        if (!leftRecursiveSubGrammar.isEmpty()){
            isLL1 = false;
            System.out.println("These have left recursion ...");
            for (GrammarTerm head : leftRecursiveSubGrammar.heads()){
                System.out.println("\tHead: " + head);
                int bodyNum = 1;
                for (GrammarBody body : leftRecursiveSubGrammar.get(head)){
                    System.out.println("\t\t|| Body "+(bodyNum++)+": " + body);
                }
            }
        }

        // Showing unused terms
        Collection<GrammarTerm> unusedTerms = GRAMMAR.unusedTerms();
        if (!unusedTerms.isEmpty()){
            System.out.println("Beware that this terms are not in use ...");
            for (GrammarTerm term :
                    unusedTerms) {
                System.out.println(" - <" + term.getName() + ">");
            }
        }

        if (isLL1){
            System.out.println("Congratulations !!! Your grammar is LL1.");
        }

    }

    private static boolean isNotLeftFactored(List<GrammarTerm> firsts){
        return firsts.stream().distinct().count() != firsts.size();
    }

    private static Grammar getLeftRecursive(){
        Grammar leftRecursive = new Grammar();
        for (GrammarTerm head : GRAMMAR.heads()){
            List<GrammarBody> leftRecursiveBodies =
                    GRAMMAR.get(head).stream()
                    .filter(body -> body.first().equals(head))
                    .collect(Collectors.toList());

            if (!leftRecursiveBodies.isEmpty()){
                leftRecursive.addAll(head, leftRecursiveBodies);
            }
        }
        return leftRecursive;
    }

    private static List<GrammarTerm> getRepeated(List<GrammarTerm> terms){
        List<GrammarTerm> visited = new ArrayList<>();
        List<GrammarTerm> result = new ArrayList<>();
        for (GrammarTerm term : terms){
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
