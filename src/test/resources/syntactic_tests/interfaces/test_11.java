K_INT
// Interfaces can't have attributes
interface Persona<T> {

    int edad = 4;

    static void getAlgo();

    dynamic Mascota<T> getMascota();

    static String filterBy(String name, int edad);

}