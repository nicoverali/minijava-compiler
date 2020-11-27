C
// Generic type can't be recursive
interface A<T> {

}

interface C<K> {

}

interface B extends A<C> {

}