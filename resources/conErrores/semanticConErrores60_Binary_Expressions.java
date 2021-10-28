///[Error:ERROR|8]
// Operadores binarios solo trabajan sobre int

class Clase  {

    static void main(){
        int a = 4 + 5 / 5 - 5 % 2 * 5;
        int b = 4 + 5 / 5 - 5 % "ERROR" * 5;
    }

}