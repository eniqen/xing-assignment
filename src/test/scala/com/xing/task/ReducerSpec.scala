package com.xing.task

import cats.Id
import com.xing.task.models.UserInfo.JobTitle
import com.xing.task.models.UserInfo.JobTitle.{ScalaDev, WebDeveloper}
import com.xing.task.reducers.Reducer
import com.xing.task.reducers.Reducer.JobReducer
import org.scalatest.{Matchers, Outcome, fixture}

import scala.util.Random

/**
  * @author Mikhail Nemenko { @literal <nemenkoma@gmail.com>}
  */
class ReducerSpec extends fixture.FlatSpec with Matchers {

  case class FixtureParam(reducer: Reducer[Id, Seq[JobTitle], Seq[Result]])

  override protected def withFixture(test: OneArgTest): Outcome = {
    val reducer = JobReducer

    withFixture(test.toNoArgTest(FixtureParam(reducer)))
  }

  it should "return empty" in { f =>
    val data = Seq.empty[JobTitle]

    val result = f.reducer.reduce(data)

    result shouldBe empty
  }

  it should "reduce one title" in { f =>
    val count = 100
    val titles = (1 to count).map(_ => ScalaDev)

    val result = f.reducer.reduce(titles)

    result shouldBe Seq(ScalaDev -> count)
  }

  it should "reduce all titles" in { f =>
    val count = 1000

    val titles = (1 to count)
      .map(_ =>
        getRandomTitle(Math.abs(Random.nextInt(10)), JobTitle.values.toArray))

    val result = f.reducer.reduce(titles)

    result.foldLeft(0)((acc, v) => acc + v._2) shouldBe count
  }

  private def getRandomTitle(number: => Int, array: Array[JobTitle]): JobTitle =
    array(number % array.length)

}
