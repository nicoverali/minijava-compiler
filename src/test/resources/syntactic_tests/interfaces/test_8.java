
// Interfaces can extend just one interface
interface Persona<T> extends Humano<T>  {

    static void getAlgo();

    dynamic Mascota<T> getMascota();

    static String filterBy(String name, int edad);

}