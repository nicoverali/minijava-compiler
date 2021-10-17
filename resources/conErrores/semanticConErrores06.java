///[Error:Clase|4]
// Herencia circular

class Clase extends Padre1 {

    static void main(){}

}

class Padre1 extends Padre2{}

class Padre2 extends Padre3{}

class Padre3 extends Clase{}