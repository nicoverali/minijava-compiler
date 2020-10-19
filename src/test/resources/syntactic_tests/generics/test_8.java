ID_MV
// Generics can't have a method/var identifier
class Persona<name> extends Humano<getSomething> {

    Animal<T_Rex> mascota;
    T4Life edad;

    Persona(Animal<T> mascota){
        this.mascota = mascota;
        edad = new Edad<T>();
    }

}