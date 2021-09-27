///[Error:this|7]
// Casting no puede ser con idMv, pero se considera como expresion y salta el error despues

class Clase extends Padre {

    static String metodo(int a){
        (clase) this;
    }

}
