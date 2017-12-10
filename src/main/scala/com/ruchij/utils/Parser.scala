package com.ruchij.utils

import com.ruchij.constants.General._
import com.ruchij.exceptions.{EmptyInputException, InputLineCountException, ParseException}
import com.ruchij.models.BoleynSalary
import com.ruchij.utils.ScalaUtils._

import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

object Parser
{
  def parse(input: List[String]): Try[BoleynSalary] =
    for {
      configValues <- ScalaUtils.toTry(input.headOption, EmptyInputException)

      (employeeCount, queryCount) <- parseIntPair(configValues)
      expectedLineCount = employeeCount + queryCount + 1

      _ <- {
        if (input.length != expectedLineCount)
          Failure(InputLineCountException(expectedLineCount, input.length))
        else Success()
      }

      superiors <- parseSuperiors(input.slice(1, employeeCount))
      salaries <- parseSalaries(input(employeeCount))
      queries <- parseQueries(input.drop(employeeCount + 1))
    }
    yield BoleynSalary(superiors, salaries, queries)

  def parseSalaries(input: String): Try[Map[Int, Int]] =
    parseIntList(input)
      .map(_.zipWithIndex.map { case (value, index) => (index + 1, value) }.toMap)

  def parseSuperiors(input: List[String]): Try[Map[Int, Int]] =
    trySequence(input.map(parseIntPair)).map(_.toMap)

  def parseQueries(input: List[String]): Try[List[(Int, Int)]] =
    trySequence(input.map(parseIntPair))

  def parseIntList(input: String): Try[List[Int]] =
    trySequence(input.split(WHITE_SPACE).toList.map(parseInt))

  def parseIntPair(input: String): Try[(Int, Int)] =
    parseIntList(input).flatMap {
      case x :: y :: Nil => Success(x, y)
      case _ => Failure(ParseException(input, classOf[(Int, Int)]))
    }

  def parseInt(input: String): Try[Int] =
    try {
      Success(input.toInt)
    } catch {
      case NonFatal(exception) => Failure(exception)
    }
}
