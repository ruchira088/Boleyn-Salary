package com.ruchij.exceptions

case class InputLineCountException(expected: Int, actual: Int) extends Exception
