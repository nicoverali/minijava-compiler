class Clase{

    private static OtraClase claseAttrEstatico;
    private OtraClase claseAttr;

    Clase(){
        claseAttrEstatico = new OtraClase();
        claseAttr = new OtraClase();
    }

    static void main(){
        claseAttrEstatico.metodo();
        claseAttrEstatico.metodoEstatico();
        crearOtraClaseEstatico().metodo();
        crearOtraClaseEstatico().metodoEstatico();
        new OtraClase().metodo();
        new OtraClase().metodoEstatico();

        // Probar en contexto dinamico
        new Clase().contextoDinamico();
    }


    dynamic void contextoDinamico(){
        claseAttr.metodo();
        claseAttr.metodoEstatico();
        claseAttrEstatico.metodo();
        claseAttrEstatico.metodoEstatico();
        crearOtraClaseEstatico().metodo();
        crearOtraClaseEstatico().metodoEstatico();
        crearOtraClase().metodo();
        crearOtraClase().metodoEstatico();
        new OtraClase().metodo();
        new OtraClase().metodoEstatico();
    }

    static OtraClase crearOtraClaseEstatico(){
        return new OtraClase();
    }

    dynamic OtraClase crearOtraClase(){
        return new OtraClase();
    }

}

class OtraClase {

    static void metodoEstatico(){}

    dynamic void metodo(){}

}