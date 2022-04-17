package scalablelanguageclub.fpe

import scalablelanguageclub.fpe.AboutImplicits.implicits.ShowSyntax


/*
 *  1. interface
 *  2. instance(s)
 *  3. syntax
 */
object AboutImplicits extends App {

  // 1. interface

  trait Showable[T] {
    def show(value: T): String
  }

  class Person(
              val name: String,
              val age: Int
              )


  // 2. instance(s)

  object Person {
    implicit val personShow : Showable[Person] = (p => s"Person(${p.name}, ${p.age})")
  }

  implicit val stringShow : Showable[String] = value => value

  // context-bound

  //def f[T](value: T)(implicit ev: Showable[T]): Unit = {
  def f[T : Showable](value: T): Unit = {
    import implicits._

    value.show()
  }

  // context-view

  trait AsString[T] {
    def asString: String
  }

  type AsStringView[T] = T => AsString[T]

  def f[T : AsStringView](value: T): Unit = println(value)

  implicit val personShow: Showable[Person] = p => s"Person(${p.name}, ${p.age})"

  object implicits {

    // 3. syntax
    implicit class ShowSyntax[T](private val value: T) extends AnyVal {
      def show()(implicit ev: Showable[T]): String = ev.show(value)
    }

  }


  val s : String = "sample"
  // import scalablelanguageclub.fpe.AboutImplicits.implicits.ShowSyntax
  println(s.show())

  val person = new Person("Vasya",12)
  println(person.show())

  // error - no implicits for show
  //123.show()


}
