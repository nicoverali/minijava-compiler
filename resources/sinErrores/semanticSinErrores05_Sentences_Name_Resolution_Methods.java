class Clase extends Padre{
    static void main(){
        metodoEstatico();
        metodoEstaticoHerencia();
        new Clase().contextoDinamico();
    }


    dynamic void contextoDinamico(){
        // En un contexto dinamico se puede llamar a todo
        metodo();
        metodoEstatico();
        metodoHerencia();
        metodoEstaticoHerencia();
    }

    static void metodoEstatico(){}

    dynamic void metodo(){}
}

class Padre extends Abuelo {}

class Abuelo {
    static void metodoEstaticoHerencia(){}

    dynamic void metodoHerencia(){}
}