Animal
// Should detect a generic type that does not exists
class Persona<T> extends Life<T> implements I1<Base>, I2<T>{

    T a = 4;
    Life<T> b;
    I1<T> c;
    I2<Base> d;

    Persona(Life<Animal> a){

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