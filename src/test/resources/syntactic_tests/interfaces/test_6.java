
// Classes can implement more than one interface without extending from a class
class Persona<T> implements Humano<T>, SerVivo<T> {

    static void getAlgo(){

    }

    dynamic Mascota<T> getMascota(){

    }

    static String filterBy(String name, int edad){

    }

}