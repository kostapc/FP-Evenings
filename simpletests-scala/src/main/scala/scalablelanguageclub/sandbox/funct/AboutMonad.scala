package scalablelanguageclub.sandbox.funct

import cats.{MonadError, MonadThrow}
import cats.data.Ior
import cats.effect.{IO, MonadThrow, Sync}


// monad vs effect
object AboutMonad {

  def main(args: Array[String]): Unit = {

    //initWithConstructor("with-constructor")
    //init1("with init funct")

    //leftIdentity
    //rightIdentity
    //associativity
    //tryError
  }

  def initWithConstructor(init: String) = {
    def createString(): String = {
      println("create string")
      init
    }

    println("creating object (constructor)")

    val ll = new Lazy (createString())

    println("now: " + ll.internal)
  }

  def init1(init: String) = {
    val lazyString: Lazy[String] = Lazy {
      println("lazy init funct")
      init
    }
    println("creating object (function)")

    //val res = lazyString.internal
    val flatMapped = lazyString.flatMap { str =>
      println("flatmapped")
      Lazy (str.toUpperCase())
    }

    val res = flatMapped.internal
    println(s"value is $res")
  }

  def tryErrorAsync = {

    // IO[String]
    val resf = Lazy(getString).getItemAsync[IO](true)

    // ---
    //val a = resf.map().map().map()
    resf.unsafeRunSync()

  }

  def getString = {
    println("here long execution")
    "sdasdf"
  }

  def tryErrorSync = {

    val res = Lazy.apply("asdf").getItemSync(false).getOrElse {
      println("effect execution failed")
      "nothing"
    }

    println(s"result is $res")
  }

}

class Lazy[A](value: => A) {
  lazy val internal: A = value

  def flatMap[B](f: (=> A) => Lazy[B]): Lazy[B] = f(internal)


  // cats effect usage
  def getItemAsync[F[_]: Sync](fine: Boolean): F[Either[Throwable, A]] = {
    Sync[F].delay {
        Either.cond(fine, internal, new IllegalStateException())
    }
    /*if(fine) {
      Ior.Right()
    } else {
      MonadThrow[F].raiseError(new IllegalStateException("ise"))
    }*/
  }

  def getItemSync(fine: Boolean): Either[Throwable, A] = {
    println("code that can cause exceptions")
    Either.cond(
      fine, {
        internal
      }, {
        new IllegalStateException("asdfsdaf")
      }
    )
  }

  def getSomething[F[_]](implicit me: MonadError[F, Throwable]): F[String] = {
    me.raiseError(new IllegalStateException("asdf"))
  }

  //def getSomething(): pure scala
}

object Lazy {

  def apply[A](value: => A): Lazy[A] = new Lazy(value) // unit

}