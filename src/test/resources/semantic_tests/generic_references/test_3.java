I2
// Should detect implementing without expliciting genericity
class Persona<T> extends Life<I2> implements I1<Life>, I2{

    T a = 4;
    Life<T> b;
    I1<T> c;
    I2<Base> d;

    Persona(Life<Base> a){

    }

    static Life<T> getLife(I1<Base> a){

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