///[SinErrores]
// xx Accesos no puede ser estaticos xx // Con logro FullStatic si puede ser estaticos

class Clase extends Padre {

    static void main(){}

    static String metodo(int a){
        (Clase) System.out.println();
    }

}

class Padre {}