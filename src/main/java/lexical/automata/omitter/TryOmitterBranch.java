package lexical.automata.omitter;

import io.code.reader.SourceCodeReader;
import lexical.LexicalException;
import lexical.automata.TryNodeBranch;
import lexical.automata.filter.LexicalFilter;
import org.jetbrains.annotations.NotNull;

public class TryOmitterBranch implements TryNodeBranch<OmitterNode, Boolean> {

    private static final int READ_AHEAD_LIMIT = 50; // This is just an estimation

    private final LexicalFilter filter;
    private final OmitterNode nextNode;

    public TryOmitterBranch(LexicalFilter filter, OmitterNode nextNode) {
        this.filter = filter;
        this.nextNode = nextNode;
    }

    @NotNull
    @Override
    public LexicalFilter getFilter() {
        return filter;
    }

    @Override
    @NotNull
    public Boolean tryDelegate(SourceCodeReader reader) {
        reader.mark(READ_AHEAD_LIMIT);
        reader.next(); // Consume character
        try {
            nextNode.omit(reader);
            return true;
        } catch (LexicalException e){
            reader.reset();
            return false;
        }
    }
}
