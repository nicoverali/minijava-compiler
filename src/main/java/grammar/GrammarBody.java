package grammar;

import com.google.common.base.Joiner;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class GrammarBody implements Iterable<GrammarTerm>{

    private final List<GrammarTerm> body = new ArrayList<>();

    public void add(GrammarTerm term){
        body.add(term);
    }

    public void addAll(Collection<GrammarTerm> terms){
        body.addAll(terms);
    }

    public GrammarTerm first(){
        return body.get(0);
    }

    public int size(){
        return body.size();
    }

    public GrammarBody subrange(int start, int end){
        GrammarBody subrange = new GrammarBody();
        subrange.addAll(body.subList(start, end));
        return subrange;
    }

    @NotNull
    @Override
    public Iterator<GrammarTerm> iterator() {
        return body.iterator();
    }

    public Stream<GrammarTerm> stream(){
        return body.stream();
    }

    @Override
    public String toString() {
        return Joiner.on(' ').join(body);
    }
}
