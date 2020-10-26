
// Valid access
class Persona {

    static int edad = 4;
    private String nombre = "";
    public static Animal mascota = new Animal();

    Persona(){

    }

    static int getEdad(){

    }

    dynamic String getNombre() {
        Persona.getEdad();
        Persona.edad = 5;
        Persona.mascota.hacerRuido();
        Persona otro = new Persona();
        Persona<A> otroMas = new Persona();
        return nombre;
    }
}