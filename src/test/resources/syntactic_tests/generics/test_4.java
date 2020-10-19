OP_GT
// Type definition must have explicit generics
class Persona<T> extends Humano<T> {

    Animal<> mascota;
    T edad;

    Persona(Animal<T> mascota){
        this.mascota = mascota;
        edad = new Edad<>();
    }

}