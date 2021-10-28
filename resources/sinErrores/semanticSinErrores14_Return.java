class Clase extends Padre{

    static void main(){
        new Clase().metodo();
        new Clase().metodoVoid();
    }

    dynamic Abuelo metodo(){
        return new Clase();
    }

    dynamic void metodoVoid(){
        return;
    }

}

class Padre extends Abuelo {}

class Abuelo {}