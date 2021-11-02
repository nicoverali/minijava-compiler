class Clase<T> {

    Clase<OtraClase> a, b, c;
    Clase<T> d, e, f;

    static void main(){}

    static Clase<T> metodo(Clase<A> a, Clase<B> b){
        Clase<OtraClase> as = new Clase<>();
        Clase<T> var = new Clase<>();
        int num = 2;

        for (Clase<OtraClase> clase = new Clase<>(); num < 4; b = (Clase<T>) a){

        }
    }

}

class OtraClase<T> extends Padre<T>{

}

class Padre{}