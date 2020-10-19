
// In this case were usign explicit generics in contructor access "new Edad<T>()"
class Persona<T> extends Humano<T> {

    Animal<T> mascota;
    T edad;

    Persona(Animal<T> mascota){
        this.mascota = mascota;
        edad = new Edad<T>();
    }

}