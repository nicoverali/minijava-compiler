I5
// Deep collision of I1 and I2 at I5
interface Test<T> {}

interface I1<T> extends Test<Integer> {}

interface I2<K> extends Test<Life> {}

interface I3 extends I1<Integer> {}

interface I4 extends I2<Life> {}

interface I5 extends I3, I4 {}

class Integer {}

class Life {}