Persona,Life
// Should detect simple circular inheritance
class Persona extends Life implements I1, I2 {

}

class Life extends Persona implements I2{

}

interface I1 extends I2 {

}

interface I2 {

}