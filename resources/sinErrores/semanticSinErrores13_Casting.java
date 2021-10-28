class Clase extends Padre{

    static void main(){
        new Clase().metodo();
    }

    dynamic void metodo(){
        Abuelo clase = new Clase();
        Abuelo padre = new Padre();
        Abuelo abuelo = new Abuelo();

        Padre p = (Padre) padre;
        Clase c = (Clase) clase;
        Padre p2 = (Clase) clase;
        Abuelo a = (Abuelo) clase;
        Abuelo a2 = (Abuelo) padre;
        Abuelo a3 = (Abuelo) abuelo;

        // Estos fallan en ejecucion pero son validos
        Clase c2 = (Clase) padre;
        Padre p3 = (Padre) abuelo;
        Clase c3 = (Clase) abuelo;
    }

}

class Padre extends Abuelo {}

class Abuelo {}