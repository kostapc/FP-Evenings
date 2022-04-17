package scalablelanguageclub.fpe

trait Evidence[T]

object Main extends App {

  import implicits._

  implicit def toInt(s: String)(implicit evidence: Evidence[String]): Int = s.toInt

  val i : Int = "123"

}


object implicits {

  implicit def d: Double = ???

  implicit def ev(implicit d: Double): Evidence[String] = ???

}
