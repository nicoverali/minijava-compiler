///[Error:this|7]
// No se puede usar this en un contexto estatico

class Clase  {

    static void main(){
        this.metodo();
    }

    static void metodo(){}

}