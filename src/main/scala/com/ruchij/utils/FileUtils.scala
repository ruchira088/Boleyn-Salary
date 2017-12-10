package com.ruchij.utils

import java.nio.ByteBuffer
import java.nio.channels.{AsynchronousFileChannel, CompletionHandler}
import java.nio.file.{Path, StandardOpenOption}

import scala.concurrent.{ExecutionContext, Future, Promise}

object FileUtils
{
  def readFile(path: Path)(implicit executionContext: ExecutionContext): Future[Array[Byte]] =
  {
    val promise = Promise[Array[Byte]]

    val fileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ)
    val byteBuffer = ByteBuffer.allocate(fileChannel.size().toInt)

    fileChannel.read(byteBuffer, 0, byteBuffer, new CompletionHandler[Integer,ByteBuffer]
    {
      override def failed(throwable: Throwable, attachment: ByteBuffer) =
        promise.failure(throwable)

      override def completed(result: Integer, attachment: ByteBuffer) =
        promise.success(attachment.array())
    })

    promise.future
  }

  def readTextFile(path: Path)(implicit executionContext: ExecutionContext): Future[List[String]] =
    readFile(path).map(byteArray => new String(byteArray).trim.split("\n").toList)
}
