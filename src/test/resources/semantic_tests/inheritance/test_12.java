I2
// Cannot extend same interface twice
class Persona extends Life implements I1, I2 {

}

class Life implements I2{

}

interface I1 extends I2, I2 {

}

interface I2 {

}