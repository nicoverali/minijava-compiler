class Clase {

    static void main(){
        if (true){
            int i = 5;
            return;
        }
        for (int i = 0; i < 4; i++) {
            int a = 4;
            return;
        }
        {
            int i = 5;
            for (int j = 0; j < 3; j++) {
                return;
            }
        }
        if (true) return;
        for (int i = 0; i < 2; i++) return;

        int i = 5;
        return;
    }

}