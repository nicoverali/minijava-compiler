K_IMPLEMENTS
// Interfaces don't use keyword implements
interface Persona<T> implements Humano<T>, SerVivo<K>, Vida<P> {

    static void getAlgo();

    dynamic Mascota<T> getMascota();

    static String filterBy(String name, int edad);

}