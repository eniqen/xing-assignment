package com.xing.task.mappers

import java.time.LocalDate

import cats.Id
import com.xing.task.mappers.extractors.{AddJobTitle, AddStartTime, Skip}
import com.xing.task.models.UserInfo
import com.xing.task.models.UserInfo.{JobTitle, UserId}

/**
  * @author Mikhail Nemenko { @literal <nemenkoma@gmail.com>}
  */
trait Mapper[F[_], In, Out] {
  def map(data: In): F[Out]
}

object Mapper {
  def apply[F[_], In, Out](implicit M: Mapper[F, In, Out]): Mapper[F, In, Out] =
    M

  object UserMapper extends Mapper[Id, Seq[UserInfo], Seq[JobTitle]] {
    override def map(data: Seq[UserInfo]): Seq[JobTitle] =
      data
        .foldLeft((Map.empty[UserId, Seq[JobTitle]], Map.empty[UserId, LocalDate])) {
          case AddStartTime(jobTitles, startTime) => jobTitles -> startTime
          case AddJobTitle(jobTitles, startTime)  => jobTitles -> startTime
          case Skip(jobTitles, startTime)         => jobTitles -> startTime
        }
        ._1
        .toSeq
        .flatMap(_._2)
  }
}
