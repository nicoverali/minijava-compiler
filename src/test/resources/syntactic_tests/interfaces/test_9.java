
// Interfaces don't need to extend any interface
interface Persona<T> {

    static void getAlgo();

    dynamic Mascota<T> getMascota();

    static String filterBy(String name, int edad);

}