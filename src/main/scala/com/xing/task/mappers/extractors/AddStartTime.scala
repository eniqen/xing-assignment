package com.xing.task.mappers.extractors

import com.xing.task.{JobTitles, StartTime}
import com.xing.task.models.UserInfo
import com.xing.task.models.UserInfo.JobTitle.WebDeveloper

/**
  * @author Mikhail Nemenko { @literal <nemenkoma@gmail.com>}
  */
object AddStartTime {
  def unapply(args: ((JobTitles, StartTime), UserInfo))
    : Option[(JobTitles, StartTime)] = args match {
    case ((j, s), UserInfo(id, WebDeveloper, _, Some(endTime))) =>
      j.get(id) -> s.get(id) match {
        case (_, None) =>
          Some((j, s + (id -> endTime)))
        case _ => None
      }
    case _ => None
  }
}
