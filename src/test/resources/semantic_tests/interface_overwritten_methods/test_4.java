Persona
// Should detect extending from interfaces where method collision occurs
interface Persona extends I1, I3 {

    static int getEdad(String anotherName);

    dynamic String getString();

}

interface I1 extends I2 {
    dynamic char getChar(int a);
}

interface I2 {
    static int getEdad(String name);
}

interface I3 {
    static char getChar(int index);
}