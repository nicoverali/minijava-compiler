///[SinErrores]
// Sentencia if no usa punto y coma final, pero se considera que cierra la sentencia y es una nueva

class Clase extends Padre {

    static String metodo(int a){
        if(true){};
    }

}
