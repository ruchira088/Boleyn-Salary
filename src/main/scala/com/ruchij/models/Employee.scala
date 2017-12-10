package com.ruchij.models

import com.ruchij.exceptions.UndefinedSalaryException
import com.ruchij.utils.ScalaUtils._

import scala.util.Try

case class Employee(id: Int, salary: Int, workers: List[Employee])

object Employee
{
  def orderedEmployeeSalaries(employee: Employee): List[Employee] =
    employee.workers.foldLeft(employee.workers) {
      case (orderedList, current) => (orderedList ++ orderedEmployeeSalaries(current))
          .sortBy(_.salary)
    }

  def create(superiors: Map[Int, Int], salaries: Map[Int, Int], id: Int): Try[Employee] =
    for {
      salary <- toTry(salaries.get(id), UndefinedSalaryException(id))
      workerIds = superiors.toList
        .filter { case (_, superior) => superior == id }
        .map { case (workerId, _) => workerId }

      workers <- trySequence(workerIds.map(create(superiors, salaries, _)))
    }
    yield Employee(id, salary, workers)

  def query(employee: Employee, id: Int, lowestOrder: Int): Option[Employee] =
    for {
      rootEmployee <- find(employee, id)

      orderedWorkers = orderedEmployeeSalaries(rootEmployee)

      result <- getValue(orderedWorkers, lowestOrder)
    }
    yield result

  def find(employee: Employee, id: Int): Option[Employee] =
    if (employee.id == id)
      Some(employee)
    else
      employee.workers.foldLeft[Option[Employee]](None) {
        case (None, worker) => find(worker, id)
        case (result, _) => result
      }
}