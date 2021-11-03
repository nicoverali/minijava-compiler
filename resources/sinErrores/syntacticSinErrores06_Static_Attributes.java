class Clase {

    public static int a;
    static Clase b;
    private Clase c;
    Clase d;

    // Metodos estaticos deberian funcionar igual
    static Clase metodo(){return new Clase();}

    static void main(){}

    dynamic int metodo2(){return 4;}
}