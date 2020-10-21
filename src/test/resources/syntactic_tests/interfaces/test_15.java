
// Classes without generic types should still work
class A {

}

class B extends D {

}

class B implements D {

}

class C implements D, E, F{

}

class B extends D implements E{

}

class C extends D implements F, G{

}