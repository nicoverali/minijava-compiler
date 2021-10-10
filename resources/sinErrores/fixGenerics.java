class Clase<A,B,C> extends Padre<A<T>, B, C> {

    OtraClase<Clase<B,C,D>, A, OtraClase<A>> a;

    Clase(){

    }

    dynamic Clase<A, OtraClase<B,C,D>, B> metodo(){
        Clase<A,A,A,OtraClase<B,C,Clase<A,B<T>>>> b = (Clase<A, A<T<B<C>>>, A<B,D<G>>>) new Clase<>();
    }

}