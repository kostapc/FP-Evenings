package scalablelanguageclub.sandbox.funct.kleisli

trait LazyMonad[A] {

  def flatMap[B](f: (=> A) => LazyMonad[B]): LazyMonad[B]

  def getValue(): A

}

object LazyMonad {

  def apply[A](a: => A): LazyMonad[A] = new LazyMonad[A] {

    override def flatMap[B](f: (=> A) => LazyMonad[B]): LazyMonad[B] = f(a)

    override def getValue(): A = a

  }

}