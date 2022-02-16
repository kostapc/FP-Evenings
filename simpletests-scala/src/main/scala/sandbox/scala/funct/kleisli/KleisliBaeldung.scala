package sandbox.scala.funct.kleisli

// https://www.baeldung.com/scala/monads

object KleisliBaeldung {

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
    //       x -> A
    val v1 = LazyMonad.apply(str)
    //       x -> A        A  ->  A
    val v2 = LazyMonad.apply(str).flatMap{v => LazyMonad.apply(v)}

    val res = v1.getValue() == v2.getValue()
    println(s"res is: $res")
  }

  def associativity = {
    val str = "17"
    //            A             A -> B           B -> A
    val v1 = LazyMonad.apply(str).flatMap{x => f(x)}.flatMap{x=>g(x)}
    //x->B             B -> A
    val v2 = f(str).flatMap{x => g(x)}

    val res = v1.getValue() == v2.getValue()
    println(s"res is: $res")
  }

}
