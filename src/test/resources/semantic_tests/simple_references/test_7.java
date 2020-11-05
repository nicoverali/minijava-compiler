I3
// Should detect a constructor parameter of a class/interface that not exists
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

interface I1 extends I4{

}

interface I2 {

}

interface I4 {

}