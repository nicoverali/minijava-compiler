ID_CLS
// Constructors don't have type, so "Persona" is excepted to be an attribute name (ID_MV)
class Persona {

    public Persona other;
    int num;
    Animal mascota;

    int Persona () {
        mascota = new Perro();
    }

    dynamic Animal getMascota(){
        return mascota;
    }

}