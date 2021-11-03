///[Error:metodo|11]
// Falta return

class Clase {


    static void main(){
        new Clase().metodo();
    }

    dynamic int metodo(){
        if (true) {
            return 5;
        } else {
            if (true) {
                return 4;
            }
        }
    }


}
