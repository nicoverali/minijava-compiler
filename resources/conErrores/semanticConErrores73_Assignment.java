///[Error:=|7]
// No se puede asignar a un metodo

class Clase{

    static void main(){
        metodo() = "ERROR";
    }

    static void metodo(){}


}

class OtraClase {

    static int metodoOtraClase(){
        return 1;
    }

}