Life
// An Interface cannot extend a Class
class Persona extends Life implements I1, I2 {

}

class Life implements I2{

}

interface I1 extends Life {

}

interface I2 {

}