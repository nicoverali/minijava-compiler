
// Classes without generic types should still work
class A {

}

class B extends D {

}

class C implements D {

}

class D implements D, E, F{

}

class E extends D implements E{

}

class F extends D implements F, G{

}