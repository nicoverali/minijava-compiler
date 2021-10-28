///[Error:a|9]
// Una llamada no puede ser un atributo

class Clase{

    public int b;

    static void main(){
        new OtraClase().a;
    }


}

class OtraClase {

    public int a;

    dynamic Clase crearClase(){
        return new Clase();
    }

}