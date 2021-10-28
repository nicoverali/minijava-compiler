class Clase extends Padre{

    private static String attrEstatico;
    private String attr;


    static void main(){
        attrEstatico = "Hola";
        attr = "Chau";

        // Probar en contexto estatico
        contextoEstatico(4);
        // Probar en contexto dinamico
        new Clase().contextoDinamico();
    }

    static void contextoEstatico(int a){
        String localStr = "Hola";
        System.printS(localStr);
        System.printI(a);
        System.printS(attrEstatico);

        System.printS(attrPadreEstatico);
    }


    dynamic void contextoDinamico(int a){
        String localStr = "Hola";
        System.printS(localStr);
        System.printI(a);
        System.printS(attrEstatico);
        System.printS(attr);

        System.printS(attrPadre);
        System.printS(attrPadreEstatico);
    }

}

class Padre {
    public static String attrPadreEstatico;
    public String attrPadre;

    Padre(){
        attrPadreEstatico = "Hola";
        attrPadre = "Chau";
    }
}