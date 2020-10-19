
// Generics can be any class identifier
class Persona<Trouble> extends Humano<Troumpet> {

    Animal<T_Rex> mascota;
    T4Life edad;

    Persona(Animal<T> mascota){
        this.mascota = mascota;
        edad = new Edad<T>();
    }

}