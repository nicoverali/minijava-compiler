Animal
// Should detect a generic type that does not exists
class Persona<T> extends Life<T> implements I1<Life>, I2<T>{

    T a = 4;
    Life<T> b;
    I1<T> c;
    I2<Life> d;

    Persona(Life<T> a){

    }

    static Life<T> getLife(I1<Animal> a){

    }

    dynamic Life<I1> getOtherLife(I2<T> b){

    }

    static void doSomething(T param){

    }

}

class Life<T>{}

interface I1<K>{}

interface I2<R>{}