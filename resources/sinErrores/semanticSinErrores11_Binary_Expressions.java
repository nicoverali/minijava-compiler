class Clase extends Padre{

    static void main(){
        int a = 4 + 5 / 5 - 5 % 2 * 5;
        boolean b = true || false && true && false || true;

        // Igualdad y desigualdad
        boolean strings = "a" != "b";
        boolean ints = (3 + 8) == (9 + 2);
        boolean chars = 'a' != 'v';
        boolean classes = new Clase() == new Padre();
        boolean classes2 = new Abuelo() == new Clase();

        // Comparaciones
        if (4 > 5){}
        if ((5+84+8) / 5 >= 4){}
        if ((5+84+8) / 5 <= 4){}
        if ((5+84+8) / 5 < 4){}
    }

}

class Padre extends Abuelo {}

class Abuelo {}