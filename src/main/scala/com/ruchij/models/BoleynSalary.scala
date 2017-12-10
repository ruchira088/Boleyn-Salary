package com.ruchij.models

import com.ruchij.utils.Parser

import scala.util.Try

case class BoleynSalary(
       superiors: Map[Int, Int],
       salaries: Map[Int, Int],
       queries: List[(Int, Int)]
)

object BoleynSalary
{
  def run(input: List[String]): Try[List[Int]] =
    for {
      BoleynSalary(superiors, salaries, queries) <- Parser.parse(input)

      boss <- Employee.create(superiors, salaries, 1)

      results = queries.scanLeft(0) {
        case (value, (idDiff, lowestOrder)) =>
          Employee.query(boss, value + idDiff, lowestOrder - 1).fold(0)(_.id)
      }
    }
    yield results.tail
}