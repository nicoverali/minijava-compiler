getEdad
// Should detect transitive invalid method overwritten
interface Persona extends I1, I3 {

    static String getEdad(String anotherName);

    dynamic char getChar(int algo);

    dynamic String getString();

}

interface I1 extends I2 {
    dynamic char getChar(int a);
}

interface I2 {
    static int getEdad(String name);
}

interface I3 {
    dynamic char getChar(int index);
}