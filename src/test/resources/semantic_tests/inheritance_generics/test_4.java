I2
// I2 interfaces have a collision
interface Test<T> {}

interface I1 extends Test<Integer> {}

interface I2 extends I1, Test<Life> {}

class Integer {}

class Life {}