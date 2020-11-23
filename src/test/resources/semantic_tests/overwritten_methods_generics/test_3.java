
// Valid case
interface I1<Q>{

    static Q doSomething(Q algo);

}

class C1 implements I1<Life> {

    static Life doSomething(Life algo){

    }

}

class Life{}