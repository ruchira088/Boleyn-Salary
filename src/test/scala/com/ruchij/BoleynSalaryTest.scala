package com.ruchij

import java.nio.file.{Path, Paths}

import com.ruchij.models.BoleynSalary
import com.ruchij.utils.{FileUtils, Parser, ScalaUtils}
import org.scalatest._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class BoleynSalaryTest extends FlatSpec with Matchers
{
  "BoleynSalary" should "return the expected output" in
  {
    val inputFiles = TestUtils.getFiles(Paths.get("src/test/resources/input"))
      .fold[List[Path]](fail("Input directory is EMPTY"))(identity[List[Path]])

    val filePaths: List[(Path, Path)] = inputFiles
      .map(path =>
        (path, Paths.get(s"src/test/resources/output/output-${TestUtils.getFileKey(path)}.txt"))
      )

    val data: Future[List[(List[String], List[Int])]] = Future.sequence {
        filePaths.map {
          case (input, output) => for {
            inputText <- FileUtils.readTextFile(input)
            outputText <- FileUtils.readTextFile(output)
            expectedResult <- Future.fromTry(ScalaUtils.trySequence(outputText.map(Parser.parseInt)))
          } yield (inputText, expectedResult)
        }
      }

    val result = for {
      testData <- data

      assertions <- Future.sequence {
        testData.map {
          case (input, output) => Future.fromTry(BoleynSalary.run(input)).map(result => assert(result == output))
        }
      }
    } yield assertions

    println(Await.result(result, 30 seconds))
  }
}
