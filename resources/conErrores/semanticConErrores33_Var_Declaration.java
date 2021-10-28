///[Error:str|17]
// Variable local duplicada en scope superior

class Main {

    static void main(){}

}

class Clase {

    dynamic void metodo(int a){
        String str = "Hello";

        if (true) {
            {
                int str = 4;
            }
        }
    }

}