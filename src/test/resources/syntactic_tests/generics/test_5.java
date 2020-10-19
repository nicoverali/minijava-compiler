OP_GT
// Type definition must have explicit generics
class Persona<T> extends Humano<T> {

    Animal<T> mascota;
    T edad;

    Persona(Animal<> mascota){
        this.mascota = mascota;
        edad = new Edad<>();
    }

}