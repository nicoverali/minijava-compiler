///[Error:OtraClase|12]
// Casting ilegal a un tipo que no conforma ni es upcasting

class Clase extends Padre{

    static void main(){
        new Clase().metodo();
    }

    dynamic void metodo(){
        Clase clase = new Clase();
        OtraClase p = (OtraClase) clase;
    }

}

class OtraClase extends Padre {}

class Padre extends Abuelo {}

class Abuelo {}