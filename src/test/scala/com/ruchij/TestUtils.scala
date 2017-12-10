package com.ruchij

import java.nio.file.{Path, Paths}

import scala.reflect.io

object TestUtils
{
  def getFiles(path: Path): Option[List[Path]] =
    io.Path(path.toFile.getAbsolutePath)
      .ifDirectory(_.files.toList.map(file => Paths.get(file.path)))

  def getFileKey(path: Path) =
    path.toFile.getName.split("-|\\.").toList match {
      case _ :: key :: _ => key
    }
}
