class Clase extends Padre{

    private int c;
    private String str;
    private Clase innerClass;

    private int attrTapado;
    public int attrTapadoPublic;

    static void main(){
        new Clase().metodo(2, "");
    }

    dynamic void metodo(int a, String b){
        ;;;;;;;;;;;;;;;;;;;;; // Sentencias vacias

        // Asignaciones
        c = 4;
        c = 6;
        c++;
        c--;
        str = "Hola";
        this.innerClass = new Class();
        OtraClase.staticBool = true;
        new OtraClase().bool = false;
        padreNum = 44;
        this.padreNum = 55;

        // Llamadas
        metodoVacio(str, c);
        padreMetodo(c);
        padreMetodo(50);
        this.padreMetodo(20);
        Clase.metodoEstaticoVacio(c);
        OtraClase.metodoEstaticoVacio();
        OtraClase.crearClase().metodoVacio(str, c);
        OtraClase.crearClase().padreMetodo(44);
        new Clase();
        new OtraClase();

        // Var locales
        String varLocal1;
        String varLocal2 = "H" + "o" + "l" + "a";
        int varLocal3 = 4 + 3 / 2 + (padreMetodo(10));
        Clase varClase = OtraClase.crearClase();
        int varLocal4 = OtraClase.crearClase().padreMetodo(10);
        int varLocal5 = OtraClase.crearClase().padreNum;
        boolean attrTapado;
        Clase attrTapadoPublic;

        // Sentencias if
        if (padreMetodo(10) == 20) System.printS("El metodo padre duplica el valor");

        if (padreMetodo(5) != 10) System.printS("El metodo padre no duplica el valor");
        else if (2 + 2 == 4) System.printS("La aritmetica funciona");
        else System.printS("Esta todo roto");

        if (c == "Chau"){
            System.printS("Dice chau");
        } else {
            System.printS("Dice hola");
        }

        // Sentencias for
        int loop = 10;
        for (int i = 0; i < loop; i++) {
            if (i == loop-1) {
                System.printS("Ultimo loop");
            }
            {
                System.printI(i);
            }
        }
        for (String forStr = ""; forStr != "AAAAAAA"; forStr = forStr + "A")
            System.printS("Loop de strings");

        // Bloques
        {
            int scopeA = 4;
            {
                scopeA = 5;
                String scopeB = "";
                {
                    System.printS(scopeB);
                }
            }
            {
                scopeA = 6;
                boolean scopeB = true;
                {
                    System.printI(scopeA);
                }
            }
            System.printI(scopeA);
        }

    }

    static void metodoEstaticoVacio(int num){}

    dynamic void metodoVacio(String str, int num){}

}

class Padre {

    public int padreNum;

    dynamic int padreMetodo(int num){
        return num * 2;
    }

}

class OtraClase {

    public static boolean staticBool;
    public boolean bool;

    static void metodoEstaticoVacio(){}

    static Clase crearClase(){
        return new Class();
    }

}