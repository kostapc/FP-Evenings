package sandbox.scala.implicits.one

import sandbox.scala.implicits.ContextWithParam
import sandbox.scala.implicits.two.{ContextJust, ContextWithParam}

// https://www.baeldung.com/scala/implicitly
// https://habr.com/ru/post/329600/

object AboutContextAndImplicit  {

  implicit val ctx: ContextJust = new ContextJust()
  implicit val otherString: ContextWithParam[String] = new ContextWithParam[String]("OTHER")
  implicit val otherInt: ContextWithParam[Int] = new ContextWithParam[Int](1313)

  def main(args: Array[String]): Unit = {
    val code = new SomeCode[Int]()
    code.bla(1)
    code.print()

    val implCode = new SomeCodeWithContext[String]()
    implCode.printAll()
  }

}

class SomeCode[T] {
  def bla(t: T)(implicit ctx: ContextJust): Unit = {
    println(s"SomeCode.bla($t) with context ${ctx.value}")
  }

  def print()(implicit other: ContextWithParam[T]): Unit = {
    other.printHello("some code")
  }
}

class SomeCodeWithContext[T: ContextWithParam] {

  def printAll(): Unit = {
    val other = implicitly[ContextWithParam[T]]
    other.printHello("some code with context")
  }

}

