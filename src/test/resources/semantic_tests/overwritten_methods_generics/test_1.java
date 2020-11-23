
// Valid case
interface I1<Q>{

    static void doSomething(Q param);

}

class C1<T>{

    dynamic T getAlgo(T algo){}

}

class C2 extends C1<Life> implements I1<Integer>{

    dynamic Life getAlgo(Life algo){}

    static void doSomething(Integer param){}

}

interface Life{}

interface Integer{}