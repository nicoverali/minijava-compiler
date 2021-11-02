///[Error:outOfScope|10]
// Variable fuera del alcance local

class Clase  {

    static void main(){
        {
            String outOfScope = "ERROR";
        }
        System.printS(outOfScope);
    }
}