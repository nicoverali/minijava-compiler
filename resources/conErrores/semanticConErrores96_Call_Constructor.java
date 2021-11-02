///[Error:;|8]
// No es una llamada valida, tiene que ser llamada a constructor directa

class Clase extends Padre{


    static void main(){
        (Clase) new Padre();
    }


}

class Padre {}