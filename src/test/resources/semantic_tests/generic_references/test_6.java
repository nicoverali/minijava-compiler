I2
// Should detect a constructor parameter with a generic type that does not explicit the generic type
class Persona<T> extends Life<I2> implements I1<Life>, I2<Persona>{

    T a = 4;
    Life<T> b;
    I1<T> c;
    I2<Life> d;

    Persona(I2 a){

    }

    static Life<T> getLife(I1<Persona> a){

    }

    dynamic Life<I1> getOtherLife(I2<T> b){

    }

    static void doSomething(T param){

    }

}

class Life<T>{}

interface I1<K>{}

interface I2<R>{}