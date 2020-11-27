A
// Generic type can't be recursive
interface A<T> {

}

interface C<K> {

}

class B implements A<A> {

}