OP_AND
// All valid types of assignation except last
class Persona extends Life {

    public int num, age, name;

    Persona(String name, int num, int age){
        num = 4;
        num += 4;
        num -= 4;
        num && 4;
    }

}