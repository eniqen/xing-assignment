package com.xing.task.mappers.extractors

import com.xing.task.{JobTitles, StartTime}
import com.xing.task.models.UserInfo

/**
  * @author Mikhail Nemenko { @literal <nemenkoma@gmail.com>}
  */
object Skip {
  def unapply(args: ((JobTitles, StartTime), UserInfo))
    : Option[(JobTitles, StartTime)] = Some(args._1)
}
