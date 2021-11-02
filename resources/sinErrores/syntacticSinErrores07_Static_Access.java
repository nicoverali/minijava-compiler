class Clase {

    static OtraClase a;
    static Clase b;
    public int num;

    dynamic int expresionesValidas(){
        int a1 = 8 +  4 * 10;
        int a2 = this.a.b();
        int a3 = new Clase<>().b.a();
        int a4 = b.a.b.a.b();
        int a5 = metodo().a.b.a();
        // int a6 = ( 4 + 5 * 6 ).a(); NO LONGER VALID
        int a7 = ( new List().b ).b.a();
    }

    static int casting(){
        Clase a = (Clase<T>) (new List<>()).a;
        Sistema a2 = (Sistema) Sistema.out;
    }

    static int expresionParentizadaEstatica(){
        int a = (Sistema.clase.metodo()).a.b();
    }

    static int expresionEstatica(){
        int a = Sistema.clase.a();
        Clase clase2 = (Clase<T>) Sistema.clase;
        Clase a2 = (Clase) (Sistema.clase.metodo()).a.b;

        int a3 = (Sistema.out.read() + Clase.b());
        boolean a4 = (true && Clase.bool());
        if(Context.getBool() == true != Context.bool){}
    }

    static int varLocalesYAsignacion(){
        int a = 4;
        int b = 5;
        Clase.metodo().num = 4;
        metodo().b = new Clase();
    }

    Clase(){
        Sistema.a = 5;
        Sistema.out.printS("Holaa");
    }

    static void main(){
        if (Context.bool){
            int a = Context.getInt();
        }
        new Clase().contextoDinamico();
        for (Clase a : Context.collections.getList()){}
    }

    dynamic void contextoDinamico(){
        int x = Id1.m1(4).m2(5, this, 'c');
    }

    static Clase metodo(){
        return new Clase();
    }

    dynamic int a(){}

    static int b(){
        return 4;
    }

    static boolean bool(){
        return false;
    }

}

class Sistema extends System {

    public static int a;
    public static System out;
    public static Clase clase;

}

class OtraClase {

    public static Clase b;

    dynamic int b(){}

}

class List {

    public Clase a;
    public OtraClase b;

}

class Context {

    public static boolean bool;

    static boolean getBool(){
        return true;
    }

    static int getInt(){
        return 24;
    }

}

class Id1 {

    static Id1 m1(int num){
        return new Id1();
    }

    dynamic int m2(int num, Clase clase, char character){
        return 44;
    }

}