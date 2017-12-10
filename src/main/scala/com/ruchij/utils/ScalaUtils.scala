package com.ruchij.utils

import scala.util.{Failure, Success, Try}

object ScalaUtils
{
  def trySequence[T](list: List[Try[T]]): Try[List[T]] =
    list match {
      case Nil => Success(List.empty)
      case x :: xs => for {
        value <- x
        rest <- trySequence(xs)
      } yield value :: rest
    }

  def toTry[T](option: Option[T], exception: => Exception): Try[T] =
    option.fold[Try[T]](Failure(exception))(Success(_))

  def getValue[T](list: List[T], index: Int): Option[T] =
    list match {
      case x :: _ if index == 0 => Some(x)
      case Nil => None
      case _ :: xs => getValue(xs, index - 1)
    }
}
