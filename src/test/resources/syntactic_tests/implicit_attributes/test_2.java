P_PAREN_OPEN
// Constructors don't have visibility, so "public Persona" should be an attribute.
class Persona {

    public Persona other;
    int num;
    Animal mascota;

    public Persona () {
        mascota = new Perro();
    }

    dynamic Animal getMascota(){
        return mascota;
    }

}