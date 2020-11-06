Life
// A Class cannot implement a Class
class Persona extends Life implements Life, I2 {

}

class Life implements I2{

}

interface I1 extends I2 {

}

interface I2 {

}