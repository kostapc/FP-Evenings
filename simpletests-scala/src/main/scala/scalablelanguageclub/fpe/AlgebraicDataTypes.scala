package scalablelanguageclub.fpe

import cats.free.Free
import cats.free.Free.liftF
import cats.{Id, ~>}

import scala.collection.mutable


object AlgebraicDataTypes {

  sealed trait KVStoreAlgebra[A]
  type KVStore[A] = Free[KVStoreAlgebra, A]

  // DSL
  object KVStoreAlgebra {
    def get[A](key: String) : KVStore[A] = liftF(Get(key))
    def remove(key: String): KVStore[Unit] = liftF(Remove(key))
    def put[A](entry: (String, A)): KVStore[Unit] = liftF(Put(entry._1, entry._2))
  }

  case class Put(key: String, value: Any) extends KVStoreAlgebra[Unit]
  case class Remove(key: String) extends KVStoreAlgebra[Unit]
  case class Get[A](key: String) extends KVStoreAlgebra[A]

  val fk: KVStoreAlgebra ~> Id = new (KVStoreAlgebra ~> Id) {

    val state = mutable.HashMap.empty[String, Any]

    override def apply[A](fa: KVStoreAlgebra[A]): Id[A] = {
      fa match {
        case Put(key, value) =>
          state.put(key, value)
          Id(())
        case Remove(key) =>
          state.remove(key)
          Id(())
        case Get(key) =>
          Id(state(key)).asInstanceOf[A]
      }
    }
  }

  def main(args: Array[String]): Unit = {
    import scalablelanguageclub.fpe.AlgebraicDataTypes.KVStoreAlgebra._

    def program = for {
      _ <- put("a" -> 1)
      a <- get[Int]("a")
      _ <- put("a" -> a * 2)
      a1 <- get[Int]("a")
    } yield a1

    println(program.foldMap(fk))
  }

}

