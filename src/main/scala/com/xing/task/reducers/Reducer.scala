package com.xing.task.reducers

import cats.Id
import com.xing.task.models.UserInfo.JobTitle
import com.xing.task.{Count, Result}

import scala.annotation.tailrec

/**
  * @author Mikhail Nemenko { @literal <nemenkoma@gmail.com>}
  */
trait Reducer[F[_], In, Out] {
  def reduce(data: In): F[Out]
}

object Reducer {
  def apply[F[_], In, Out](implicit R: Reducer[F, In, Out]): Reducer[F, In, Out] = R

  object JobReducer extends Reducer[Id, Seq[JobTitle], Seq[Result]] {
    override def reduce(data: Seq[JobTitle]): Seq[Result] = {
      @tailrec
      def loop(from: Seq[JobTitle], acc: Map[JobTitle, Count]): Seq[Result] =
        from match {
          case Seq() => acc.toSeq
          case jobTitleKey +: tail =>
            loop(tail,
              acc
                .get(jobTitleKey)
                .fold(acc + (jobTitleKey -> 1))(current =>
                  acc.updated(jobTitleKey, current + 1)))
        }
      loop(data, Map.empty)
    }
  }
}