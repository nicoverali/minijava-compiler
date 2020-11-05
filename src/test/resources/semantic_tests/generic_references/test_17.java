Animal
// Should detect a generic type that does not exists
interface Persona<T> extends I1<Animal>, I2<Persona>{

    static Life<T> getLife(I1<Persona> a);

    dynamic Life<I1> getOtherLife(I2<T> b);

    static void doSomething(T param);

}

class Life<T>{}

interface I1<K>{}

interface I2<R>{}