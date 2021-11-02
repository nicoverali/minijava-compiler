///[Error:metodoInexistente|10]
// El metodo accedido no fue declarado

class Clase {
    static void main(){
        new Clase().metodoDinamico();
    }

    dynamic void metodoDinamico(){
        this.metodoInexistente();
    }
}