///[Error:=|11]
// Un atributo o variable en una expresion parentizada no es asignable

class Clase {

    static void main(){
        metodo("Hola");
    }

    static void metodo(String b){
        (b) = "String";
    }


}