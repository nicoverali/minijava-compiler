///[Error:metodo|10]
// Metodo duplicado. Sobrecarga incorrecta

class Clase extends Padre {

    Clase(){}

    Clase(int a){}

    static void metodo(int b){}

    static void main(){}

}

class Padre {
    dynamic void metodo(int a){}
}