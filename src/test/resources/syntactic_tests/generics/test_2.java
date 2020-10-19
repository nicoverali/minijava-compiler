OP_GT
// Class definition must have explicit generics
class Persona<> extends Humano<T> {

    Animal<T> mascota;
    T edad;

    Persona(Animal<T> mascota){
        this.mascota = mascota;
        edad = new Edad<>();
    }

}