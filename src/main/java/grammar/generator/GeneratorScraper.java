package grammar.generator;

import com.google.common.base.CaseFormat;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import grammar.Grammar;
import grammar.GrammarBody;
import grammar.GrammarTerm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class GeneratorScraper {

    private final Grammar grammar;
    private List<GrammarTerm> visited;

    public GeneratorScraper(Grammar grammar) {
        this.grammar = grammar;
    }

    public List<GeneratorMethod> createMethods(GrammarTerm initialTerm){
        visited = new ArrayList<>();
        List<GeneratorMethod> methods = new ArrayList<>();
        addMethods(methods, initialTerm);
        return Lists.reverse(methods);
    }

    private void addMethods(List<GeneratorMethod> methods, GrammarTerm currentTerm){
        visited.add(currentTerm);
        GeneratorMethod method = new GeneratorMethod(toCamelCase(currentTerm));
        Collection<GrammarBody> bodies = grammar.get(currentTerm);
        for (GrammarBody body : bodies){
            List<String> bodyFirsts = grammar.firstOf(body).stream().map(GrammarTerm::getName).collect(Collectors.toList());
            checkForLambda(method, bodyFirsts);
            checkForRecursiveCall(body, methods);
            method.addMethodBody(createMethodBody(body, bodyFirsts));
        }
        method.setFollowTokens(grammar.followOf(currentTerm).stream().map(GrammarTerm::getName).collect(Collectors.toSet()));
        methods.add(method);
    }

    private void checkForLambda(GeneratorMethod method, List<String> firsts){
        if (firsts.contains("EOF")){
            method.confirmLambda();
        }
    }

    private void checkForRecursiveCall(GrammarBody body, List<GeneratorMethod> methods){
        body.stream().filter(GrammarTerm::isNonTerminal)
                .filter(Predicates.not(visited::contains))
                .forEach(term -> addMethods(methods, term));
    }

    private GeneratorMethodBody createMethodBody(GrammarBody body, List<String> bodyFirsts){
        GeneratorMethodBody methodBody = new GeneratorMethodBody();
        bodyFirsts.stream()
                .filter(token -> !token.equals("EOF"))
                .forEach(methodBody::addToken);

        for (GrammarTerm term : body){
            if (term.isNonTerminal()){
                methodBody.addNonTerminalAction(toCamelCase(term));
            } else {
                if (!term.getName().equals("EOF"))
                    methodBody.addTokenAction(term.getName());
            }
        }

        return methodBody;
    }

    public static String toCamelCase(GrammarTerm term){
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, term.getName());
    }

}
