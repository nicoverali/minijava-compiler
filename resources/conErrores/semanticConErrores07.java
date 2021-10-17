///[Error:Clase|12]
// Herencia circular definiendo primero otra clase si correcta

class OtraClase extends Padre1{}



class Padre1 extends Padre2{}

class Padre2 extends Padre3{}

class Clase extends Padre1 {

    static void main(){}

}

class Padre3 extends Clase{}