C
// Generic type can't be recursive
class A<T> {

}

interface C<K> {

}

class B extends A<C> {

}