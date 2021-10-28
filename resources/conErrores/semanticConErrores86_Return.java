///[Error:return|11]
// El return no puede ser vacio si el metodo tiene un tipo de retorno distinto de void

class Clase{

    static void main(){
        new Clase().metodo();
    }

    dynamic int metodo(){
        return;
    }


}