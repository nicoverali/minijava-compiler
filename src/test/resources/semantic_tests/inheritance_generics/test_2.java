I3
// Both I1 and I2 collision so I3 can't extend both
interface Test<T> {}

interface I1<T> extends Test<Integer> {}

interface I2<K> extends Test<Life> {}

interface I3<Q> extends I1<Q>, I2<Q>{}

class Integer{}

class Life{}