package scalablelanguageclub.fpe

import cats.effect.IO

import scala.io.StdIn

object ForComprehensions extends App {

  def print(s: => String): IO[Unit] = IO.delay(_root_.scala.Predef.print(s))

  def println(line: => String): IO[Unit] = IO.delay(_root_.scala.Predef.println(line))

  def readln: IO[String] = IO.delay(StdIn.readLine())

  def program: IO[Unit] = for {
    _ <- print(">")
    line <- readln
    _ <- if (line == "stop") IO.raiseError(sys.error("Stopped!")) else IO.unit
    _ <- println(s"input was: $line")
  } yield ()

}
