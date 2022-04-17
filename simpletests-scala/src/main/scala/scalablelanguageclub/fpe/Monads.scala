package scalablelanguageclub.fpe

trait MyApplicative[F[_]] {
  def pure[A](a: A): F[A]
}

trait MyFunctor[F[_]] extends MyApplicative[F] {
  def map[A,B](fa: F[A])(f: A => B):F[B]
}

// bind used in haskel instead of flatMap, because of it means bind one function to another
trait MyBind[F[_]] {
  def flatMap[A,B](fa: F[A])(f: A => F[B]): F[B]
}

trait MyMonad[F[_]] extends MyBind[F] with MyFunctor[F] { self =>
  override def map[A, B](fa: F[A])(f: A => B) : F[B] = self.flatMap(fa)(f.andThen(pure))
}
