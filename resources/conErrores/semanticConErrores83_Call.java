///[Error:b|9]
// Una llamada no puede ser un atributo

class Clase{

    public int b;

    static void main(){
        new OtraClase().crearClase().b;
    }


}

class OtraClase {

    public int a;

    dynamic Clase crearClase(){
        return new Clase();
    }

}