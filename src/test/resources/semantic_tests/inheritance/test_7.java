I1,I2
// Should detect simple circular interface implementation
class Persona extends Life implements I1, I2 {

}

class Life implements I2{

}

interface I1 extends I2 {

}

interface I2  extends I1{

}