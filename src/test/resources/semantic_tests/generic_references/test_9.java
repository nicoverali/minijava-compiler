
// Valid uses of generics in interfaces
interface Persona<T> extends I1<Base>, I2<Base>{

    static Life<T> getLife(I1<Base> a);

    dynamic Life<Base> getOtherLife(I2<T> b);

    static void doSomething(T param);

}

class Base {

}

class Life<T>{}

interface I1<K>{}

interface I2<R>{}