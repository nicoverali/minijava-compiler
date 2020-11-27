Life
// Should detect a method return type that is generic but does not explicit the generic type
class Persona<T> extends Life<I2> implements I1<Life>, I2<Persona>{

    T a = 4;
    Life<T> b;
    I1<T> c;
    I2<Base> d;

    Persona(Life<Base> a){

    }

    static Life getLife(I1<Base> a){

    }

    dynamic Life<Base> getOtherLife(I2<T> b){

    }

    static void doSomething(T param){

    }

}

class Base {

}

class Life<T>{}

interface I1<K>{}

interface I2<R>{}