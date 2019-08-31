package com.xing.task

import cats.Id
import cats.kernel.Monoid
import com.xing.task.mappers.Mapper
import com.xing.task.mappers.Mapper.UserMapper
import com.xing.task.models.UserInfo
import com.xing.task.models.UserInfo.JobTitle
import com.xing.task.models.UserInfo.JobTitle.WebDeveloper
import com.xing.task.readers.Reader
import com.xing.task.readers.Reader.InMemmoryUserReader
import com.xing.task.reducers.Reducer
import com.xing.task.reducers.Reducer.JobReducer
import com.xing.task.services.JobService

/**
  * @author Mikhail Nemenko { @literal <nemenkoma@gmail.com>}
  */
object XingApp extends App {

  val reader: Reader[Id, Seq[UserInfo]] = InMemmoryUserReader
  val mapper: Mapper[Id, Seq[UserInfo], Seq[JobTitle]] = UserMapper
  val reducer: Reducer[Id, Seq[JobTitle], Seq[Result]] = JobReducer

  private val service = new JobService[Id](reader, mapper, reducer)

  println(service.getTopK(3))
}
import cats.implicits._

object Test {
  def getTopK(k: Int): Seq[Result] =
    InMemmoryUserReader.read.sorted
      .groupBy(_.id)
      .flatMap {
        case (_, titles) =>
          for {
            webDeveloper <- titles.find(_.jobTitle == WebDeveloper)
            endTime <- webDeveloper.end
            result <- titles
              .dropWhile(exp =>
                (exp.start isBefore endTime) || exp.jobTitle == WebDeveloper)
              .headOption
          } yield result.jobTitle
      }
      .aggregate(Map.empty[JobTitle, Count])(
        (acc, v) =>
          acc.get(v).fold(acc + ((v, 1)))(prev => acc.updated(v, prev + 1)),
        Monoid[Map[JobTitle, Int]].combine(_, _))
      .toSeq
      .sortBy(_._2)(Ordering.Int.reverse)
      .take(k)
}
