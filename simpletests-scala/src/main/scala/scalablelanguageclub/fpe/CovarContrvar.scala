package scalablelanguageclub.fpe

object CovarContrvar extends App {

  val a = new A()
  val b = new B()
  val c = new C()

  val foo: Foo[C] = new Foo(c)
  val bar: Bar[C] = new Bar[B] {
    override def bar[B <: A](x: B): Unit = ???
  }

}

class A
class B extends A
class C extends B

class X

class Foo[+A](val x: A)
abstract class Bar[-A] {
  def bar[B <: A](x: B)
}