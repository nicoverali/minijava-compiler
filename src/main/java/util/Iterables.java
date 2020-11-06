package util;

public class Iterables {

    public static <T> T getLast(Iterable<T> iterable){
        T last = null;
        for (T element : iterable){
            last = element;
        }
        return last;
    }

    public static <T> T getLast(Iterable<T> iterable, T defaultValue){
        T last = defaultValue;
        for (T element : iterable){
            last = element;
        }
        return last;
    }


}
