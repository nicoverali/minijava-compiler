P_DOT
// Static access can't have genericity, so Persona<A> should be consider a varibable declaration
class Persona {

    static int edad = 4;
    private String nombre = "";
    public static Animal mascota = new Animal();

    Persona(){

    }

    static int getEdad(){

    }

    dynamic String getNombre() {
        Persona<A>.mascota;
        return nombre;
    }
}