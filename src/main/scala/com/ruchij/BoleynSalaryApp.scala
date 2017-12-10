package com.ruchij

import java.nio.file.Paths

import com.ruchij.models.BoleynSalary
import com.ruchij.utils.FileUtils

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object BoleynSalaryApp
{
  def main(args: Array[String]): Unit =
  {
    val result = for {
      input <- FileUtils.readTextFile(Paths.get("resources/input.txt"))
      results <- Future.fromTry(BoleynSalary.run(input))
    }
    yield results.mkString("\n")

    println(Await.result(result, 1 minute))
  }
}
