package sandbox.scala.implicits.two

class ContextJust {
  val value: String = "add context value"
}

object ContextJust {
  def compFunct(): Unit = {
    println("comp funct called")
  }
}

class ContextWithParam[T](
             val toPrint: T
           ) {
  def printHello(prefix: String): Unit = {
    println(s"$prefix >> $toPrint")
  }
}
