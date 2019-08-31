package com.xing.task.models

import java.time.LocalDate

import com.xing.task.models.UserInfo.{JobTitle, UserId}

import scala.collection.immutable._

/**
  * @author Mikhail Nemenko { @literal <nemenkoma@gmail.com>}
  */
case class UserInfo(id: UserId,
                    jobTitle: JobTitle,
                    start: LocalDate,
                    end: Option[LocalDate])

object UserInfo {

  case class UserId(id: Int) extends AnyVal

  implicit val byIdAndStartTime: Ordering[UserInfo] =
    Ordering.by[UserInfo, (Int, Long)](u => u.id.id -> u.start.toEpochDay)

  import enumeratum._
  sealed abstract class JobTitle(title: String) extends EnumEntry {
    override val entryName: String = title
  }

  object JobTitle extends Enum[JobTitle] {
    case object WebDeveloper extends JobTitle("Web developer")
    case object J2EEDeveloper extends JobTitle("J2EE developer")
    case object DataScientist extends JobTitle("Data Scientist")
    case object GoDev extends JobTitle("Go developer")
    case object ScalaDev extends JobTitle("Scala developer")
    case object PHPDev extends JobTitle("PHP developer")

    override def values: IndexedSeq[JobTitle] = findValues
  }
}
