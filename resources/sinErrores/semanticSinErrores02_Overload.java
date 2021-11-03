class Class extends ClassPadre1{

    Class(){} // Constructor original

    Class(int a){} // Sobrecarga constructor

    Class(int a, String b){} // Sobrecarga constructor

    dynamic void metodo(){} // Metodo original
    dynamic void metodo(int a){} // Parametro extra
    dynamic ClassPadre1 metodo(String b){return new ClassPadre1();} // Tipo de retorno distinto
    static String metodo(boolean c){return "";} // Tipo estatico
    static Class metodo(ClassPadre1 padre){return new Class();} // Todo junto

    dynamic String metodoPadre(){return "";} // Sobrecarga sobre metodo del padre
    static Class metodoPadre(String b, Class a){return new Class();} // Sobrecarga sobre metodo del padre

    static String metodoSobreescrito(String a, int b){return "";} // Sobreescribe metodo del padre
}

class ClassPadre1 extends ClassPadre2 {}

class ClassPadre2 extends Object{

    dynamic void metodoPadre(int a){} // Metodo original

    static String metodoSobreescrito(String b, int a){return "";}

    static void main(){}

}

class SystemParticular extends System {

    static int read(String a){return 4;} // Sobrecarga metodo predefinido
    dynamic String read(int b){return "Hola";} // Sobrecarga metodo predefinido

}