package scalablelanguageclub.sandbox.funct

object EntryPoint {

  def main(args: Array[String]): Unit = {

    println("main!")

/*    val values = List(
      "one", "two", "three"
    )

    val upperValues = for (
      v <- values
    ) yield {
      v.toUpperCase
    }

    for {
      uv <- upperValues
      b <- List(tryInt()(uv))
    } yield b*/

    //val ss: () => String = () => "64"

    // s -> uppper case
    val smc = StrangeMonad.delay("64")
      .map(a => {
        println("toInt")
        a.toInt
      })
      .map(a => {
        println("toChar")
        a.toChar
      })
    // uc -> int
    // i -> char

    println("asdf")

    val res = smc.compile

    println(s"res $res")

  }

  def tryInt[B >: Int](): String => B = {
    case "ONE" => 1
    case "TWO" => 2
    case "THREE" => 3
    case s => try {
      s.toInt
    } catch {
      case _: Throwable => -1
    }
  }

}

trait StrangeMonad[A] {

  def map[B](f: A => B): StrangeMonad[B]

  def flatMap[B](f: A => StrangeMonad[B]): StrangeMonad[B]

  def compile: A

}

object StrangeMonad {

  def delay[A](a: => A): StrangeMonad[A] = new StrangeMonad[A] {
    override def map[B](f: A => B): StrangeMonad[B] = {
      println("map")
      StrangeMonad.delay(f(a))
    }

    override def flatMap[B](f: A => StrangeMonad[B]): StrangeMonad[B] = {
      println("flatmap")
      f(a)
    }

    override def compile: A = a
  }

  def pure[A](a: A): StrangeMonad[A] = new StrangeMonad[A] {
    override def map[B](f: A => B): StrangeMonad[B] = {
      println("map")
      //StrangeMonad.pure(None).flatMap(_ => StrangeMonad.pure(f(a)))
      StrangeMonad.pure(f(a))
    }

    override def flatMap[B](f: A => StrangeMonad[B]): StrangeMonad[B] = {
      println("flatmap")
      f(a)
    }

    override def compile: A = a
  }

}


class Code {


}

object Code {
  def apply(): Code = {
    new Code()
  }
}

// read


