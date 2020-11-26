
// Valid case
interface I1<Q>{

    static Q doSomething(Q algo);

}

interface I2 extends I1<Life> {

    static Life doSomething(Life algo);

}

class Life{}