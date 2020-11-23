
// Valid case
interface Test<T> {}

interface I1<T> extends Test<Integer> {}

interface I2<K> extends Test<Integer> {}

interface I3<Q> extends I1<Q>, I2<Q>{}

class Integer {}