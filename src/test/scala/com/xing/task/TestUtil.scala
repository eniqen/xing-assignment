package com.xing.task

import cats.Id
import com.xing.task.mappers.Mapper
import com.xing.task.mappers.Mapper.UserMapper
import com.xing.task.models.UserInfo
import com.xing.task.models.UserInfo.JobTitle
import com.xing.task.readers.Reader
import com.xing.task.readers.Reader.InMemmoryUserReader
import com.xing.task.reducers.Reducer
import com.xing.task.reducers.Reducer.JobReducer
import com.xing.task.services.JobService

/**
  * @author Mikhail Nemenko { @literal <nemenkoma@gmail.com>}
  */
trait TestUtil {
  def withService(
      test: (Reader[Id, Seq[UserInfo]],
             Mapper[Id, Seq[UserInfo], Seq[JobTitle]],
             Reducer[Id, Seq[JobTitle], Seq[Result]]) => Unit): Unit = {

    val reader: Reader[Id, Seq[UserInfo]] = InMemmoryUserReader
    val mapper: Mapper[Id, Seq[UserInfo], Seq[JobTitle]] = UserMapper
    val reducer: Reducer[Id, Seq[JobTitle], Seq[Result]] = JobReducer

    test(reader, mapper, reducer)
  }

  def withData(
      test: (Mapper[Id, Seq[UserInfo], Seq[JobTitle]],
             Reducer[Id, Seq[JobTitle], Seq[Result]]) => Unit): Unit = {
    val mapper = UserMapper
    val reducer = JobReducer

    test(mapper, reducer)
  }

  def withJobService(test: JobService[Id] => Unit): Unit = {
    val reader = InMemmoryUserReader
    val mapper = UserMapper
    val reducer = JobReducer

    val jobService = new JobService[Id](reader, mapper, reducer)

    test(jobService)
  }
}
