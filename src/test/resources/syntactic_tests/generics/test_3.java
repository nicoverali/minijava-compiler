OP_GT
// Inheritance definition must have explicit generics
class Persona<T> extends Humano<> {

    Animal<T> mascota;
    T edad;

    Persona(Animal<T> mascota){
        this.mascota = mascota;
        edad = new Edad<>();
    }

}