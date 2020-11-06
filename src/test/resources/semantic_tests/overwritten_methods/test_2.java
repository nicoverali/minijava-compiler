getName
// Should dectect direct invalid staticness
class Persona extends Life {

    static int getEdad(){

    }

    static String getName(){

    }

}

class Life extends Obj {

    dynamic String getName(){

    }

}

class Obj {

    static int getEdad(){

    }

}