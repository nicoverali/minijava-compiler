///[Error:metodoInexistente|6]
// El metodo accedido no fue declarado

class Clase {
    static void main(){
        new OtraClase().malParametros("Hola", 4);
    }
}

class OtraClase {

    dynamic void malParametros(String a, int n, boolean b){}
}