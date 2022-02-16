package sandbox.scala.basics

trait MyStream[+A] {
  def uncons: Option[(A, MyStream[A])]

  def toList: List[A]

  def take(n: Int): MyStream[A]

  def drop(n: Int): MyStream[A]

  def append[B >: A](element: => B): MyStream[B]

  def takeWhile(p: A => Boolean): MyStream[A]

  def #::[B >: A](a: => B): MyStream[B]

  def +[B >: A](other: MyStream[B]): MyStream[B]
}

object MyStream {
  def empty[A]: MyStream[A] =
    new MyStream[A] {
      override def uncons: Option[Nothing] = None

      override def toList: List[A] = List.empty

      override def take(n: Int): MyStream[A] = this

      override def drop(n: Int): MyStream[A] = this

      override def append[B >: A](element: => B): MyStream[B] = cons(element, empty)

      override def takeWhile(p: A => Boolean): MyStream[A] = this

      override def #::[B >: A](a: => B): MyStream[B] = this

      override def +[B >: A](other: MyStream[B]): MyStream[B] = this
    }

  def cons[A](hd: => A, tl: => MyStream[A]): MyStream[A] = new MyStream[A] {
    override lazy val uncons = Some((hd, tl))

    override lazy val toList: List[A] = {
      def compose(currentPair: Option[(A, MyStream[A])]): List[A] = currentPair match {
        case None => List.empty
        case Some((h, v)) => h :: v.toList
      }

      compose(uncons)
    }

    override def take(n: Int): MyStream[A] =
      if (n == 0) empty
      else uncons.map(pair => cons(pair._1, pair._2.take(n - 1))).get

    override def drop(n: Int): MyStream[A] =
      if (n == 0)
        cons(uncons.value._1, uncons.value._2)
      else
        uncons.map(pair => pair._2.drop(n - 1)).get

    override def append[B >: A](element: => B): MyStream[B] =
      if (uncons.isEmpty)
        cons(element, empty)
      else
        uncons.map(pair => cons(pair._1, pair._2.append(element))).get

    def +[B >: A](other: MyStream[B]): MyStream[B] =
      if (uncons.value._2.uncons.isEmpty)
        cons(uncons.value._1, other)
      else
        uncons.map(pair => cons(pair._1, pair._2 + other)).get

    override def takeWhile(p: A => Boolean): MyStream[A] = uncons.map(pair =>
      if (p(pair._1))
        empty.append(pair._1) + pair._2.takeWhile(p)
      else
        pair._2.takeWhile(p)
    ).get

    override def #::[B >: A](a: => B): MyStream[B] = cons(a, this)
  }
}
