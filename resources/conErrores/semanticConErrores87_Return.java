///[Error:true|11]
// El tipo del return no conforma con el tipo de retorno del metodo

class Clase{

    static void main(){
        new Clase().metodo();
    }

    dynamic String metodo(){
        return true;
    }


}