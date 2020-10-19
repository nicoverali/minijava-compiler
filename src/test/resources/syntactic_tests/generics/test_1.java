
// This are all valid use cases of generics
class Persona<T> extends Humano<T> {

    Animal<T> mascota;
    public T edad;

    Persona(Animal<T> mascota){
        this.mascota = mascota;
        edad = new Edad<>();
    }

}