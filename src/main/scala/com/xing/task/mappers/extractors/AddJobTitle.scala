package com.xing.task.mappers.extractors

import com.xing.task.{JobTitles, StartTime}
import com.xing.task.models.UserInfo
import com.xing.task.models.UserInfo.JobTitle.WebDeveloper

/**
  * @author Mikhail Nemenko { @literal <nemenkoma@gmail.com>}
  */
object AddJobTitle {
  def unapply(args: ((JobTitles, StartTime), UserInfo))
    : Option[(JobTitles, StartTime)] = args match {
    case ((j, s), v) =>
      j.get(v.id) -> s.get(v.id) match {
        case (titles, Some(startTime))
            if v.start.isAfter(startTime) && titles
              .forall(_.isEmpty) && v.jobTitle != WebDeveloper =>
          titles
            .map(v.jobTitle +: _)
            .orElse(Some(Seq(v.jobTitle)))
            .map(j.updated(v.id, _) -> s)
        case _ => None
      }
  }
}
