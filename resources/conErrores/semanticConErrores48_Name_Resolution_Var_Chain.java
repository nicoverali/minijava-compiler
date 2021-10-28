///[Error:privateAttr|7]
// Variable privada en la clase

class Clase  {

    static void main(){
        System.printS(new OtraClase().privateAttr)
    }
}

class OtraClase {

    private String privateAttr;

}