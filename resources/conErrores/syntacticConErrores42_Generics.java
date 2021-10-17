///[Error:(|9]
// La declaracion de un constructor no puede tener genericidad. Igual se lo
// considera como atributo y falla en los parentesis

class Clase<T> extends Padre {

    String a;

    Clase<T>(){

    }

    static OtraClase<T> metodo(Clase<T> a){
        for () {

        }
    }

}
