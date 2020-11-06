getEdad
// Should detect transitive invalid staticness
class Persona extends Life {

    dynamic int getEdad(Life anothherName, int force){

    }

    dynamic String getName(String anotherName, int b){

    }

}

class Life extends Obj {

    dynamic String getName(String a, int b){

    }

}

class Obj {

    static int getEdad(Life a, int f){

    }

}