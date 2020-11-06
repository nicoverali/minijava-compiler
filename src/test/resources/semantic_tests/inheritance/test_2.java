I1
// A Class cannot extend an interface
class Persona extends I1 implements I1, I2 {

}

class Life implements I2{

}

interface I1 extends I2 {

}

interface I2 {

}