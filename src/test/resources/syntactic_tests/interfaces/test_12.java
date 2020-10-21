ID_CLS
// Interfaces can't have constructors
interface Persona<T> {

    Persona(){

    }

    static void getAlgo();

    dynamic Mascota<T> getMascota();

    static String filterBy(String name, int edad);

}