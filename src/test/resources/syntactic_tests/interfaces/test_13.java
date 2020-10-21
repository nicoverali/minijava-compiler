P_BRCKT_OPEN
// Interfaces methods don't have a body
interface Persona<T> {

    static void getAlgo() {
        System.out.println("Hello World!");
    };

    dynamic Mascota<T> getMascota();

    static String filterBy(String name, int edad);

}