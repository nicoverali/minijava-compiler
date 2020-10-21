
// This is a valid interface
interface Persona<T> extends Humano<T>, SerVivo<K>, Vida<P> {

    static void getAlgo();

    dynamic Mascota<T> getMascota();

    static String filterBy(String name, int edad);

}