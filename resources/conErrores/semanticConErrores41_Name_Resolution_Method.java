///[Error:malParametros|6]
// El metodo accedido no tiene la sobrecarga usada

class Clase {
    static void main(){
        new OtraClase().malParametros("a", true);
    }
}

class OtraClase {

    dynamic void malParametros(String b, int c){

    }

}