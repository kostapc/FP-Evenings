package scalablelanguageclub.sandbox.funct.kleisli

object KleisliRobNoris {

  def main(args: Array[String]): Unit = {
    leftIdentity
    rightIdentity
    associativity
  }

  def f(str: String): LazyMonad[Int] = LazyMonad.apply(str.toInt)
  def g(num: Int): LazyMonad[String] = LazyMonad.apply(num.toString)

  def leftIdentity = {
    val str = "17"
    //      x -> A         A -> B
    val v1 = LazyMonad.apply(str).flatMap{x => f(x)}
    // x -> B
    val v2 = f(str)

    val res = v1.getValue() == v2.getValue()
    println(s"res is: $res")
  }

  def rightIdentity = {
    val str = "17"
    val m = LazyMonad(str)
    //       x -> A        A  ->  A
    val v1 = m.flatMap{v => LazyMonad(v)}
    //       x -> A
    val v2 = m


    val res = v1.getValue() == v2.getValue()
    println(s"res is: $res")
  }

  def associativity = {
    val str = "17"
    val m = LazyMonad(str)
    //       A     A -> B     B -> A
    val v1 = m.flatMap{x => f(x)}.flatMap{x => g(x)}
    //x->B             B -> A
    val v2 = m.flatMap{x =>
      f(x).flatMap{y => g(y)}
    }

    val res = v1.getValue() == v2.getValue()
    println(s"res is: $res")
  }

}
