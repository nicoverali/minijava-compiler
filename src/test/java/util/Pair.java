package util;

public class Pair<X,Y> {

    public static <X,Y> Pair<X,Y> of(X first, Y second){
        return new Pair<>(first, second);
    }

    public final X first;
    public final Y second;

    private Pair(X first, Y second){
        this.first = first;
        this.second = second;
    }

}
