Persona
// A Class cannot extend itself
class Persona extends Persona implements I1, I2 {

}

class Life implements I2{

}

interface I1 extends I2 {

}

interface I2 {

}