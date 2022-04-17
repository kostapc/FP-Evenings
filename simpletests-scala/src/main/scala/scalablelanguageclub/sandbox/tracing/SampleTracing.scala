package scalablelanguageclub.sandbox.tracing

import java.util.UUID


class Span[F[_]](tracingContext: TracingContext) {

  def onStart() = ???

  def onFinish() = ???

}

object Span {

  def start[F[_], A](f: A => F[A]): A => F[A] = {
    implicit val ctx: TracingContext = TracingContext(UUID.randomUUID(), null)
    span(f)
  }

  def span[F[_], B](f: B => F[B])(implicit ctx: TracingContext): B => F[B] = {
    implicit val newCtx: TracingContext = ctx.span()
    f
  }

}

case class TracingContext(
                           id: UUID,
                           parent: UUID
                         ) {

  def span(): TracingContext = {
    TracingContext(
      id = UUID.randomUUID(),
      parent = this.id
    )
  }

}

object JustRun {
  def main(args: Array[String]): Unit = {
    Span.start[Res, String] {
      /*Span.span {
        new Res("started: " + _)
      }*/
      ???
    }
  }

  def execFun(f: String => Unit): Unit = f("HERE")
}


class Res[T](v: T)
