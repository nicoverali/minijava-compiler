P_PAREN_OPEN
// Constructors can't have visibility, thus public Persona should be consider an attribute
class Persona {

    public int edad = 4;
    private int a = 3, b, c, e = 18;
    static int f = 4, g = 5;
    public static Persona p = new Persona();
    String name = "Nico";
    String apellido;
    Animal mascota1 = new Perro(), mascota2;

    public Persona(String name) {

    }

    static int getEdad(){

    }

    dynamic int getA(){

    }


}
