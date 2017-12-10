package com.ruchij

import com.ruchij.models.Employee
import org.scalatest.{FlatSpec, Matchers}

class EmployeeTest extends FlatSpec with Matchers
{
  "Employee.orderedEmployeeSalaries" should "return employees order by salary" in {

    val adam = Employee(1, 100, List.empty)
    val bob = Employee(2, 150, List.empty)
    val chris = Employee(3, 110, List(adam))
    val david = Employee(4,  95, List(bob))
    val erin = Employee(5, 105, List(chris))
    val frank = Employee(6, 120, List(david, erin))
    val gary = Employee(7, 100, List(frank))

    val orderedSalaries = Employee.orderedEmployeeSalaries(gary)

    orderedSalaries shouldEqual List(david, adam, erin, chris, frank, bob)
  }
}
