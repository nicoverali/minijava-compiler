getName
// Should detect direct implementing method with wrong header
class Persona implements I1, I2 {

    dynamic int getName(){

    }

    static I3 getAlgo() {

    }

    static int getEdad(String name) {

    }

}

interface I1 extends I3 {

    dynamic String getName();

}

interface I2 {

    static I3 getAlgo();

    static int getEdad(String name);

}

interface I3 {
    static int getEdad(String name);
}