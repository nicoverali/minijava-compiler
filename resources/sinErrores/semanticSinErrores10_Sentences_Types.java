class Clase{

    static void main(){
        String a = "Hola";
        int b = 4;
        char c = 'c';
        boolean b1 = true;
        boolean b2 = false;
        Clase clase1 = null;
        Clase clase2 = new Clase();
    }


    dynamic String metodo1(){
        return "String";
    }
    dynamic int metodo2(){
        return 33;
    }
    dynamic char metodo3(){
        return 'a';
    }
    dynamic boolean metodo4(){
        return true;
    }
    dynamic boolean metodo5(){
        return false;
    }
    dynamic Clase metodo6(){
        return null;
    }
    dynamic Clase metodo7(){
        return new Clase();
    }

}