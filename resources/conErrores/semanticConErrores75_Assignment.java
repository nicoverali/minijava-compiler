///[Error:=|11]
// La expresion no conforma el tipo de la asignacion

class Clase{

    static void main(){
        new Clase().metodo(4);
    }

    static void metodo(int a){
        a = true || false == true;
    }


}