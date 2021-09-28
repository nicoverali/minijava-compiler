class Clase<T> {

    Clase<OtraClase> a, b, c;
    Clase<T> d, e, f;

    static Clase<T> metodo(Clase<A> a, Clase<B> b){
        Clase<OtraClase> as = new Clase<>();
        Clase<T> a = 4;

        for (Clase<OtraClase> a = new Clase<>(); a < 4; b = (Clase<T>) a){

        }
    }

}

class Clase<T> extends Padre<T>{

}