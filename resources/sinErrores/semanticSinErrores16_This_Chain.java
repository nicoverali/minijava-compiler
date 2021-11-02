class Clase {

    public int a;

    static void main(){
        new Clase().metodo();
    }

    dynamic void metodo(){
        System.printI(this.a);
    }

}