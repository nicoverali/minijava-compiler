I1
// Cannot implement same interface twice
class Persona extends Life implements I1, I1 {

}

class Life implements I2{

}

interface I1 extends I2 {

}

interface I2 {

}