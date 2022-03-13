package scalablelanguageclub.sandbox.tracing

import java.util.UUID


class Span[F[_]](tracingContext: TracingContext) {

  def onStart() = ???

  def onFinish() = ???

}

object Span {

  def start[F[_], A, B: TracingContext](f: A => F[B]): A => F[B] = {
    implicit val ctx: TracingContext = TracingContext(UUID.randomUUID(), null)
    span(f)
  }

  def span[F[_], A, B: TracingContext](f: A => F[B])(implicit ctx: TracingContext): A => F[B] = {
    implicit val newCtx: TracingContext = ctx.span()
    20
    f()
  }

}

case class TracingContext (
                          val id: UUID,
                          val parent: UUID
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
    Span.start[Res, String] { a =>
      Span.span {
        new Res("asdf")
      }(a)
    }

  }


  def execFun(f: String => Unit): Unit = f("HERE")

}


class Res[T](v: T)