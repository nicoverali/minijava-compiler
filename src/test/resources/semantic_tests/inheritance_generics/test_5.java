
// Valid case
interface I1<T>{}

interface I2<T>{}

class Life{}
class Integer{}

class C1 implements I1<Life>{}

class C2 implements I1<Integer>, I2<Integer> {}

class C3 extends C1 implements I1<Life> {}

class C4 extends C2 implements I2<Integer> {}