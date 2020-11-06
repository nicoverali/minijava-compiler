I1
// An Interface cannot extend itself
class Persona extends Life implements I1, I2 {

}

class Life implements I2{

}

interface I1 extends I1 {

}

interface I2 {

}