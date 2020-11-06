Persona
// Should detect direct unimplemented method
class Persona implements I1, I2 {

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