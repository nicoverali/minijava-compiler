class Clase{

    private static OtraClase claseAttrEstatico;
    private OtraClase claseAttr;

    Clase(){
        claseAttrEstatico = new OtraClase();
        claseAttr = new OtraClase();
    }

    static void main(){
        System.printS(claseAttrEstatico.attr);
        System.printS(claseAttrEstatico.attrEstatico);

        System.printS(crearOtraClaseEstatico().attr);
        System.printS(crearOtraClaseEstatico().attrEstatico);

        System.printS(new OtraClase().attr);
        System.printS(new OtraClase().attrEstatico);

        // Probar en contexto dinamico
        new Clase().contextoDinamico(9);
    }


    dynamic void contextoDinamico(int a){
        System.printS(claseAttr.attr);
        System.printS(claseAttr.attrEstatico);
        System.printS(claseAttrEstatico.attr);
        System.printS(claseAttrEstatico.attrEstatico);

        System.printS(crearOtraClase().attr);
        System.printS(crearOtraClase().attrEstatico);
        System.printS(crearOtraClaseEstatico().attr);
        System.printS(crearOtraClaseEstatico().attrEstatico);

        System.printS(new OtraClase().attr);
        System.printS(new OtraClase().attrEstatico);
    }

    static OtraClase crearOtraClaseEstatico(){
        return new OtraClase();
    }

    dynamic OtraClase crearOtraClase(){
        return new OtraClase();
    }

}

class OtraClase {

    public static String attrEstatico;
    public String attr;

}