///[Error:metodoHerencia|7]
// No se puede acceder a un metodo dinamico en un contexto estatico

class Clase extends Padre{
    static void main(){
        metodoHerenciaEstatico();
        metodoHerencia();
    }
}

class Padre {

    static void metodoHerenciaEstatico(){}

    dynamic void metodoHerencia(){}

}