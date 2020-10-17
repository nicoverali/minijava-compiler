
// This are valid attributes and constructor
class Persona {

    public Persona other;
    int num;
    Animal mascota;

    Persona () {
        mascota = new Perro();
    }

    dynamic Animal getMascota(){
        return mascota;
    }

}