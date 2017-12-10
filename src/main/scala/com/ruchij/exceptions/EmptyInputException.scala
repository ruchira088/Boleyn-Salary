package com.ruchij.exceptions

object EmptyInputException extends Exception
{
  override def getMessage: String = "Empty input"
}