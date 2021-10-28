///[Error:Object|11]
// El tipo del return no conforma con el tipo de retorno del metodo

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

        System.printB(abueloAbuelo == abueloAbuelo);
        System.printB(abueloAbuelo == padreAbuelo);
        System.printB(abueloAbuelo != claseAbuelo);
        System.printB(abueloAbuelo == otroAbuelo);
        System.printB(abueloAbuelo != padrePadre);
        System.printB(abueloAbuelo == clasePadre);
        System.printB(abueloAbuelo != otroPadre);
        System.printB(abueloAbuelo == clase);
        System.printB(abueloAbuelo != otraClase);

        System.printB(padreAbuelo == claseAbuelo);
        System.printB(padreAbuelo == otroPadre);
        System.printB(padreAbuelo == clase);
        System.printB(padreAbuelo == otraClase);
        System.printB(padreAbuelo == padreAbuelo);

        System.printB(clasePadre == padrePadre);
        System.printB(otroPadre != padrePadre);
        System.printB(clase == padrePadre);
        System.printB(otraClase != padrePadre);

        System.printB(claseAbuelo == otroAbuelo);
        System.printB(otroPadre != claseAbuelo);
        System.printB(clasePadre == otroPadre);


        System.printB(clase == clase);
        System.printB(clase != clase);

        System.printB(otraClase == otraClase);
        System.printB(otraClase != otraClase);
    }


}

class Padre extends Abuelo {}

class Abuelo {}

class OtraClase extends Padre {}