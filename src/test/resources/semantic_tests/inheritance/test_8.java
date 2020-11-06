I1,I2,I3
// Should detect comple circular interface implementation
class Persona extends Life implements I1, I2 {

}

class Life implements I2{

}

interface I1 extends I2 {

}

interface I2  extends I3{

}

interface I3 extends I1 {

}