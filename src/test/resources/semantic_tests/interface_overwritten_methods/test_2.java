something
// Should detect direct overwritten invalid method
interface Persona extends I1, I3 {

    static int getEdad(String anotherName);

    dynamic char getChar(int algo);

    static I3 something(String ivalidParamaterOverwritten);

    dynamic String getString();

}

interface I1 extends I2 {
    dynamic char getChar(int a);

    static I3 something();
}

interface I2 {
    static int getEdad(String name);
}

interface I3 {
    dynamic char getChar(int index);
}