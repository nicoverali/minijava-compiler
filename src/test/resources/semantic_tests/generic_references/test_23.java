C
// Generic type can't be recursive
class A<T> {

}

class C<K> {

}

class B extends A<C> {

}