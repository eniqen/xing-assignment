package com.xing.task

import com.xing.task.models.UserInfo.JobTitle.{DataScientist, J2EEDeveloper}
import org.scalatest.{FreeSpec, Matchers}

/**
  * @author Mikhail Nemenko { @literal <nemenkoma@gmail.com>}
  */
class JobServiceSpec extends FreeSpec with Matchers with TestUtil {

  "Job service" - {
    "should correct reduce all job titles" in withJobService(service => {
      val jobResult = service.startJob

      jobResult shouldBe List(J2EEDeveloper -> 2, DataScientist -> 1)
    })

    "should incorrect compute data when unsorted" in withService {
      case (data, mapper, reducer) =>
        val result = reducer.reduce(mapper.map(data.read))

        result shouldBe empty
    }

    "should return top1" in withJobService { service =>
      val result = service.getTopK(1)

      result should have size 1

      result shouldBe Seq(J2EEDeveloper -> 2)
    }
  }
}
