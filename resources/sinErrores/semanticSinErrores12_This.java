class Clase extends Padre{

    static void main(){
        new Clase().metodo();
    }

    dynamic void metodo(){
        Clase clase = this;
        Padre padre = this;
        Abuelo abuelo = this;
        if (this == this){}
    }

}

class Padre extends Abuelo {}

class Abuelo {}