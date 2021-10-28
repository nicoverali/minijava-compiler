///[Error:attrPrivado|7]
// Variable de padre privada

class Clase extends Padre {

    static void main(){
        System.printS(attrPrivado)
    }
}

class Padre {

    private static attrPrivado;

}