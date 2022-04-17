package scalablelanguageclub.fpe

import java.math.BigInteger
import scala.collection.mutable
import scala.util.control.TailCalls.{TailRec, done, tailcall}


object RecursiveFactorial {

  def f0(n: Long) : BigInteger = {

    val cache = mutable.HashMap.empty[BigInteger, TailRec[BigInteger]]

    cache.put(BigInteger.ZERO, done(BigInteger.ZERO))
    cache.put(BigInteger.ONE, done(BigInteger.ONE))

    def f0(i: BigInteger) : TailRec[BigInteger] =
      cache.get(i) match {
        case Some(result) => result
        case None => for {
          a <- tailcall(f0(i subtract BigInteger.ONE))
          b <- tailcall(f0(i subtract BigInteger.TWO))
          sum = a add b
          _ = cache.put(i, done(sum))
        } yield sum
      }

    f0(BigInteger.valueOf(n)).result

  }

  println(f0(60_100L))

}
