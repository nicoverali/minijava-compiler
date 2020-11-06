package grammar;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class GrammarBody implements Iterable<GrammarTerm>{

    private final List<GrammarTerm> body = new ArrayList<>();

    /**
     * Adds a {@link GrammarTerm} at the end of this body.
     *
     * @param term a {@link GrammarTerm} which will be added at the end
     */
    public void add(GrammarTerm term){
        body.add(term);
    }

    /**
     * Adds at the end of the body every {@link GrammarTerm} in the given collection. The order will be determine
     * by the collection {@link Iterator}.
     *
     * @param terms a {@link Collection} of {@link GrammarTerm} which will be added at the end
     */
    public void addAll(Collection<GrammarTerm> terms){
        body.addAll(terms);
    }

    /**
     * @return the first {@link GrammarTerm} in this body
     */
    public GrammarTerm first(){
        return body.get(0);
    }

    /**
     * Checks whether this body has at least one {@link GrammarTerm} or not.
     *
     * @return true if the body has at least one {@link GrammarTerm}, false otherwise
     */
    public boolean isEmpty(){
        return body.isEmpty();
    }

    /**
     * @return the number of {@link GrammarTerm} in this body
     */
    public int size(){
        return body.size();
    }

    public GrammarBody subrange(int start, int end){
        GrammarBody subrange = new GrammarBody();
        subrange.addAll(body.subList(start, end));
        return subrange;
    }

    /**
     * @return a {@link Stream} of all {@link GrammarTerm} present in this body
     */
    public Stream<GrammarTerm> stream(){
        return body.stream();
    }

    @Override
    public Iterator<GrammarTerm> iterator() {
        return body.iterator();
    }


    @Override
    public String toString() {
        return Joiner.on(' ').join(body);
    }
}
