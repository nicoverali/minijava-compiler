C3
// C1 and C3 collision because they instantiate the same interface with different generic
interface I1<T>{}

interface I2<T>{}

class Life{}
class Integer{}

class C1 implements I1<Life>{}

class C3 extends C1 implements I1<Integer> {}