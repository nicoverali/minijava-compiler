K_PUBLIC
// Interfaces can't have attributes
interface Persona<T> {

    public int edad = 4;

    static void getAlgo();

    dynamic Mascota<T> getMascota();

    static String filterBy(String name, int edad);

}