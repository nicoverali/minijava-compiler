///[Error:metodo|10]
// Metodo duplicado. Sobrecarga incorrecta

class Clase extends Padre {

    Clase(){}

    Clase(int a){}

    dynamic int metodo(int b){}

    static void main(){}

}

class Padre {
    dynamic void metodo(int a){}
}