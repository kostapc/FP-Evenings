import scala.language.implicitConversions

object RunSimple {

  def main(args: Array[String]): Unit = {
    //val res = adder(2,1)
    //val add2 = adder(1, _:Int)
    //val res = add2("1")(2)
    //Console.println(s"res = $res")
    // ===============
    //testImplicit()
  }

  def adder(m: Int, n: Int):Int = {
    Console.println("adder called")
    m + n
  }

  //def add2: Int => Int = adder(1, _:Int)
  //def add2() = Int => Int = adder(1, _:Int)
  def add2(v: String): Int => Int = {
    x => adder(x, v.toInt)
  }

}

/*case class A(i: Int)
case class B(i: Int)

implicit def aToB(a: A): B = {
  println("implicit call")
  B(a.i)
}

def testImplicit(): Unit = {
  val a = A(1)
  val b: B = a
  println(s"res = $b")
}*/
