///[Error:=|7]
// No se puede asignar a un metodo

class Clase{

    static void main(){
        new OtraClase().metodoOtraClase() = "ERROR";
    }

    static void metodo(){}


}

class OtraClase {

    static void metodoOtraClase(){}

}