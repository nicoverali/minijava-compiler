///[Error:!=|17]
// Los tipos no son compatibles

class Clase extends Padre {

    static void main(){
        Abuelo abueloAbuelo = new Abuelo();
        Abuelo padreAbuelo = new Padre();
        Abuelo claseAbuelo = new Clase();
        Abuelo otroAbuelo = new OtraClase();
        Padre padrePadre = new Padre();
        Padre clasePadre = new Clase();
        Padre otroPadre = new OtraClase();
        Clase clase = new Clase();
        OtraClase otraClase = new OtraClase();

        System.printB(clase != otraClase);
    }


}

class Padre extends Abuelo {}

class Abuelo {}

class OtraClase extends Padre {}