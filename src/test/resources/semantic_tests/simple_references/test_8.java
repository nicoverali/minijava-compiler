I4
// Should detect a method parameter of a class/interface that not exists
class Persona extends Life implements I1, I2{

    Life a;
    private static I1 i;

    Persona(){

    }

    dynamic void getAlgo(I3 interf){

    }

    static Life getOtraCosa(I4 interf){

    }

}

class Life {

}

interface I1 extends I3{

}

interface I2 {

}

interface I3 {

}