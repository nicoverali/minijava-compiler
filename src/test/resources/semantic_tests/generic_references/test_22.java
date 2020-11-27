A
// Generic type can't be recursive
class A<T> {

}

class B extends A<A> {

}