///[Error:&&|8]
// Operadores binarios || y && solo trabajan sobre boolean

class Clase  {

    static void main(){
        boolean a = true || false && true && false || true;
        boolean b = true || false && null && false || true;
    }

}