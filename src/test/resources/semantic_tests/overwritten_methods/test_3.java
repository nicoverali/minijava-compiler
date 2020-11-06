getName
// Should dectect direct invalid return type
class Persona extends Life {

    static int getEdad(){

    }

    dynamic char getName(){

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