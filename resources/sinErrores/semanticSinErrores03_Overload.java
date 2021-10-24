class Class extends Padre{
    dynamic void metodo(int a, String b){} // Metodo original
    static Class metodo(String b, int c){return new Class();}
}

class Padre {
    static void main(){}
}