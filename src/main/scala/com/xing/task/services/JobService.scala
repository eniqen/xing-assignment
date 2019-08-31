package com.xing.task.services

import cats.Monad
import com.xing.task.Result
import com.xing.task.mappers.Mapper
import com.xing.task.models.UserInfo
import com.xing.task.models.UserInfo.JobTitle
import com.xing.task.readers.Reader
import com.xing.task.reducers.Reducer

/**
  * @author Mikhail Nemenko { @literal <nemenkoma@gmail.com>}
  */
class JobService[F[_]: Monad](
    reader: Reader[F, Seq[UserInfo]],
    mapper: Mapper[F, Seq[UserInfo], Seq[JobTitle]],
    reducer: Reducer[F, Seq[JobTitle], Seq[Result]]
) {

  import cats.syntax.flatMap._
  import cats.syntax.functor._
  def startJob(implicit O: Ordering[UserInfo]): F[Seq[Result]] =
    for {
      data <- reader.read
      mapped <- mapper.map(data.sorted)
      result <- reducer.reduce(mapped)
    } yield result

  def getTopK(k: Int)(implicit O: Ordering[UserInfo]): F[Seq[Result]] =
    startJob.map(_.sortBy(_._2)(Ordering.Int.reverse).take(k))
}
