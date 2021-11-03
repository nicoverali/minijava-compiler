class Class extends ClassPadre1{

    Class attribute; // Tapa atributo con el mismo nombre

    String a; // Tapa atributo con mismo nomrbe pero distinto tipo

    Object object; // Clases
    System system; // predefinidas

    Class(int a){

    }

    static void debugPrint(int a){} // Sobre-escribe metodo predefinido

    static void method(int a){} // Sobre-escribe metodo correctamente

    dynamic Object getObject(){return new Object();} // Clases
    static System getSystem(){return new System();}  // predefinidas

    dynamic ClassPadre2 test(String a, int b, ClassPadre1 padre, Class clazz, Class clazz2){return new ClassPadre2();} // Muchos args

}

class ClassPadre1 extends ClassPadre2 {

    Class attribute;

    int a;

    ClassPadre1(String b){

    }

    static void method(int a){}

    static void method2(int a){}

}

class ClassPadre2 extends Object{

    ClassPadre2 (){

    }

    static void main(){}

}

class SystemParticular extends System {

    static int read(){return 4;} // Sobreescribe metodo predefinido

}