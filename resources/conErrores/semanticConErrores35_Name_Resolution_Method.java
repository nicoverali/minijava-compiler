///[Error:metodo|8]
// No se puede acceder a un metodo dinamico en un contexto estatico

class Clase extends Padre{

    static void main(){
        metodoEstatico();
        metodo();
    }

    static void metodoEstatico(){}

    dynamic void metodo(){}
}

class Padre {}