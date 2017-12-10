package com.ruchij.exceptions

case class ParseException(input: String, clazz: Class[_]) extends Exception
{
  override def getMessage: String = s"""Unable to parse \"$input\" as ${clazz.getName} """
}