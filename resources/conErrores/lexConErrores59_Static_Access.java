///[Error:var|8]
// xx Los tipos de clase no pueden ser accesos estaticos xx Si bien es verdad, el error se detecta en 'var'
// ya que se considera que se quiso hacer un acceso estatico y luego la palabra 'var' no tiene sentido

class Clase {

    dynamic void main(){
        Clase.a var = 4;
    }

}
