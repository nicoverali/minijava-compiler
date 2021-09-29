class Clase {

    static int a;

    static int expresionesValidas(){
        int a = 'a' +  4 * null;
        int a = this.a.b();
        int a = new Clase<>().b.a();
        int a = b.a.b.a;
        int a = metodo().a.b;
        int a = ( 4 + 5 * 6 ).a();
        int a = ( new List().b ).b;
    }

    static int casting(){
        int a = (Clase<T>) (new List<>()).a;
        int a = (Clase) system.out.println();
    }

    static int expresionParentizadaEstatica(){
        int a = (System.out.println()).b().c;
    }

    static int expresionEstatica(){
        int a = System.out.println();
        int a = (Clase<T>) System.out.println();
        int a = (Clase) (System.out.println()).a.b;

        int a = (System.out + Clase.b());
        int a = (true + Clase.metodo());
        if(Context.getBool() - metodo() + Context.bool * (Clase<T>) (Context.a)){}
    }

    static int varLocalesYAsignacion(){
        Clase a = 4;
        int b = 5;
        Clase.a().b = 4;
        a().b = 5;
    }

    Clase(){
        System.a;
        System.out.println();
    }

    dynamic void main(){
        if (Context.bool){
            int a = Context.getInt();
        }
        x = Id1.m1(4).m2(5, this, 'c');

        for (Clase a : Context.collections.getList()){}
    }

}