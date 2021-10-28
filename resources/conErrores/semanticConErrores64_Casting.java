///[Error:Padre|12]
// No se puede hacer upcasting. Igual hay que preguntar esto

class Clase extends Padre{

    static void main(){
        new Clase().metodo();
    }

    dynamic void metodo(){
        Clase clase = new Clase();
        Padre p = (Padre) clase;
    }

}

class OtraClase {}

class Padre extends Abuelo {}

class Abuelo {}