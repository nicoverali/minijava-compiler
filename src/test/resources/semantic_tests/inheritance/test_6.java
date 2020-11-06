Persona,Life,Example
// Should detect complex circular inheritance
class Persona extends Life implements I1, I2 {

}

class Life extends Example implements I2{

}

interface I1 extends I2 {

}

interface I2 {

}

class Example extends Persona {

}