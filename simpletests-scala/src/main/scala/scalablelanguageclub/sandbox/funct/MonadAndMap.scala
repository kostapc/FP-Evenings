package scalablelanguageclub.sandbox.funct

object MonadAndMap {

  def main(args: Array[String]): Unit = {

    println("--------------- 1")
    fm1
    println("--------------- 2")
    fm2

  }

  def fm1 = {
    val monad = Some.apply("FM1")

    println("monad created with defined value")

    val monadOfContainer = monad.flatMap1 (JustContainer.pack)

    println("now monad with container ")

    val value = monadOfContainer.get().value

    println(s"value is '$value'")
  }

  def fm2 = {
    val monad = Some.apply("FM2")

    println("monad created with defined value")

    val monadOfContainer = monad.flatMap2 { vv =>
      JustContainer.pack(vv)
    }

    println("now monad with container ")

    val value = monadOfContainer.get().value

    println(s"value is '$value'")
  }

}

trait Some[A] {

  def flatMap1[B](f: A => Some[B]): Some[B]

  def flatMap2[B](f: (=> A) => Some[B]): Some[B]

  def get(): A

}

object Some {

  def apply[A](a: A): Some[A] = new Some[A] {
    private val state = a

    override def flatMap1[B](f: A => Some[B]): Some[B] = {
      println("Some.flatMap1")
      f(state)
    }


    override def flatMap2[B](f: (=> A) => Some[B]): Some[B] = {
      println("Some.flatMap2")
      f(state)
    }

    override def get(): A = state
  }

}

// -------------------------------

case class JustContainer[A](value: A)

object JustContainer {
  def pack[A](v: A): Some[JustContainer[A]] = {
    println(s"just container pack: $v")
    Some.apply(JustContainer(v))
  }
}
