package sequence;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public class ListMarkableSequence<T> implements MarkableSequence<T>, AppendableSequence<T> {

    private int cursor = 0;
    private final LinkedList<T> elements = new LinkedList<>();
    private final Deque<Marker> markers = new LinkedList<>();

    public ListMarkableSequence(){}
    public ListMarkableSequence(Iterable<T> elements){
        this.appendAll(elements);
    }

    @Override
    public void append(T element) throws IllegalArgumentException{
        if (element == null) throw new IllegalArgumentException("Appended elements can't be null.");
        elements.add(element);
        flushMarkers();
    }

    public void flushMarkers(){
        markers.removeIf(marker -> marker.position + marker.readAheadLimit < cursor);
        int flushTo = markers.isEmpty() ? cursor : markers.peek().position;
        for (int i=0; i < flushTo; i++){
            elements.remove();
        }
        // Update indexes because removing elements alter positions
        markers.forEach(marker -> marker.position-=flushTo);
        cursor -= flushTo;
    }

    @Override
    public void appendAll(Iterable<T> elements) {
        StreamSupport.stream(elements.spliterator(), false)
                .filter(Objects::nonNull)
                .forEach(this.elements::addLast);
        flushMarkers();
    }

    @Override
    public void mark(int readAheadLimit) throws IllegalArgumentException {
        if (readAheadLimit < 0) throw new IllegalArgumentException("Read ahead limit must be positive.");
        markers.add(new Marker(cursor, readAheadLimit));

    }

    @Override
    public void reset() throws IllegalStateException {
        if (markers.isEmpty()) throw new IllegalStateException("There must be at least one marker to reset the sequence.");
        cursor = markers.pollLast().position;
    }

    @Override
    public Optional<T> next() {
        if (hasNext()){
            return Optional.of(elements.get(cursor++));
        }
        return Optional.empty();
    }

    @Override
    public boolean hasNext() {
        return cursor < elements.size();
    }

    @Override
    public Optional<T> peek() {
        if (hasNext()){
            return Optional.of(elements.get(cursor));
        }
        return Optional.empty();
    }

    @Override
    public String toString() {
        String elementsString = IntStream.range(0, elements.size())
                .mapToObj(idx -> idx == cursor
                        ? "[" + elements.get(idx) + "]"
                        : elements.get(idx) + "")
                .collect(Collectors.joining(", "));

        return "{ " + elementsString + " }";
    }

    private static class Marker {
        int position;
        int readAheadLimit;

        public Marker(int position, int readAheadLimit) {
            this.position = position;
            this.readAheadLimit = readAheadLimit;
        }

        @Override
        public String toString() {
            return "Marker{" +
                    "position=" + position +
                    ", readAheadLimit=" + readAheadLimit +
                    '}';
        }
    }
}
