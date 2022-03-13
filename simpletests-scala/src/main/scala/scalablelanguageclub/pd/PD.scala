package scalablelanguageclub.pd

import cats.Parallel
import cats.effect.concurrent.Semaphore
import cats.effect.{Async, Concurrent, ContextShift, ExitCode, IO, IOApp, Timer}
import cats.implicits._

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.DurationInt

case class Fork(num: Int)
case class Phil(num: Int)


/*object Run extends App {

  private implicit val timer: Timer[IO] = IO.timer(ExecutionContext.global)
  private implicit val ctx: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  new PD[IO].run(
    4, 1000
  ).unsafeRunSync()

}*/

object Run extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = new PD[IO](5, 100, 200)
    .run *> ExitCode.Success.pure[IO]

}

class ForkResource[F[_]: Async: Concurrent](
                       semaphores: List[Semaphore[F]]
                     ) {

  def use(id: Int): F[Fork] = {
    // get resource
    semaphores(id).acquire.map(_ => Fork(id))
  }

  def free(fork: Fork): F[Int] = {
    // release resource
    semaphores(fork.num).release.map(_ => fork.num)
  }
}

object ForkResource {

  def apply[F[_]: Async: Concurrent](philsCount: Int): F[ForkResource[F]] = {
    List.tabulate(philsCount+1)(_ => Semaphore[F](1)).sequence.map(s => new ForkResource(s))
  }

}

class PD[F[_]: Timer: ContextShift: Async: Concurrent: Parallel](
                                                        philCount: Int,
                                                        eatTime: Int,
                                                        maxStarvingTime: Int
                                                      ) {
//class PD[F[_]] (implicit timer: Timer[F], ctx: ContextShift[F], async: Async[F], concurrent: Concurrent[F]) {


  def forkFor(phil: Phil): (Int, Int) = if(philCount==1) {
    0 -> 1
  } else {
    phil match {
      case Phil(pos) if pos == philCount - 1 => 0 -> pos
      case Phil(pos) => pos -> (pos + 1)
    }
  }

  def eat(phil: Phil, forkResource: ForkResource[F]): F[Unit] = {
    val (f1, f2) = forkFor(phil)
    for {
      start <- Timer[F].clock.realTime(TimeUnit.MILLISECONDS)
      r1 <- forkResource.use(f1)
      r2 <- forkResource.use(f2)
      end <- Timer[F].clock.realTime(TimeUnit.MILLISECONDS)
      _ <- Async[F].whenA((end - start) > maxStarvingTime)(
        new RuntimeException(s"phil ${phil.num} is dead").raiseError[F, Unit])
      _ <- Async[F].delay(println(s"${phil.num} start eating"))
      _ <- Timer[F].sleep(eatTime.millis)
      _ <- Async[F].delay(println(s"${phil.num} end eating"))
      _ <- forkResource.free(r1)
      _ <- forkResource.free(r2)
      finish <- Timer[F].clock.realTime(TimeUnit.MILLISECONDS)
      _ <- Async[F].delay(println(s"${phil.num} forks [$f1,$f2] free (${finish - start})"))
    } yield ()

  }

  def think(phil: Phil): F[Unit] = {
    Async[F].delay(println(s"${phil.num} start thinking")) >>
      Timer[F].sleep(300.millis) >> Async[F].delay(println(s"${phil.num} end thinking"))
  }

  def philProgram(phil: Phil, forkResource: ForkResource[F]): F[Unit] = {
    (eat(phil, forkResource) *> think(phil)) >> philProgram(phil, forkResource)
  }

  def run: F[Unit] = {

    for {
      s <- ForkResource[F](philCount)
      phils = List.tabulate(philCount)(Phil.apply)
      result <- phils.map(p => {
        Timer[F].sleep((eatTime * p.num).millis) *> philProgram(p, s)
      }).parSequence.as(())
    } yield result

  }
}
