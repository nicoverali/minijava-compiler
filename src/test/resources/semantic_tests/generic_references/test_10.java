I2
// Should detect extending from an generic interface without expliciting generic type
interface Persona<T> extends I1<Life>, I2{

    static Life<T> getLife(I1<Persona> a);

    dynamic Life<I1> getOtherLife(I2<T> b);

    static void doSomething(T param);

}

class Life<T>{}

interface I1<K>{}

interface I2<R>{}