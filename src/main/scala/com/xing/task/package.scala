package com.xing

import java.time.LocalDate

import com.xing.task.models.UserInfo.{JobTitle, UserId}

/**
  * @author Mikhail Nemenko { @literal <nemenkoma@gmail.com>}
  */
package object task {
  type Count  = Int
  type Result = (JobTitle, Count)
  type ByUserId[T] = Map[UserId, T]
  type JobTitles = ByUserId[Seq[JobTitle]]
  type StartTime = ByUserId[LocalDate]

  sealed trait Ord
  object Ord {
    case object ASC extends Ord
    case object DESC extends Ord
  }
}
