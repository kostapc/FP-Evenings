package scalablelanguageclub.fpe


// 1. interface
trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}

object AboutFunctor extends App {

  // 2. instance
  implicit val optionFunctor: Functor[Option] = new Functor[Option] {
    override def map[A,B](fa: Option[A])(f: A => B): Option[B] = ??? // todo
  }

  // 3. syntax
  // "extends AnyVal" - inject current class functions to every class, visible in current package
  implicit class FunctorSyntax[A, F[_]](private val fa: F[A]) extends AnyVal {
    def map[B](f: A => B)(implicit F: Functor[F]): F[B] = F.map(fa)(f)
  }

  def f[A, F[_]: Functor](fa: F[A]): F[Unit] = fa.map(println)

}


